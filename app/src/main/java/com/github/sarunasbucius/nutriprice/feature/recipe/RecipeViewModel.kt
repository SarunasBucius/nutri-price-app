package com.github.sarunasbucius.nutriprice.feature.recipe

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sarunasbucius.nutriprice.core.model.Recipe
import com.github.sarunasbucius.nutriprice.core.navigation.AppComposeNavigator
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.network.service.NutriPriceClient
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarController
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarEvent
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeUi(
    val recipe: Recipe = Recipe("", emptyList(), emptyList(), ""),
    val isLoading: Boolean = true
)

@HiltViewModel
class RecipeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val nutriPriceClient: NutriPriceClient,
    private val navigation: AppComposeNavigator<NutriPriceScreen>,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val recipeId: String = savedStateHandle["recipeId"] ?: ""

    var uiState by mutableStateOf(RecipeUi())
        private set

    init {
        fetchRecipe()
    }

    fun fetchRecipe() {
        viewModelScope.launch(ioDispatcher) {
            uiState = uiState.copy(
                isLoading = true
            )
            viewModelScope.launch(ioDispatcher) {
                val result = nutriPriceClient.fetchRecipe(recipeId)
                result.onSuccess {
                    uiState = uiState.copy(
                        recipe = data,
                        isLoading = false
                    )
                }.onFailure {
                    Log.e("RecipeViewModel", "fetch recipe: ${message()}")
                    viewModelScope.launch {
                        SnackbarController.sendEvent(SnackbarEvent("ERROR: something went wrong"))
                        navigation.navigateUp()
                    }
                }
            }
        }
    }
}