package com.github.sarunasbucius.nutriprice.feature.recipe.viewPreparedRecipe

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.github.sarunasbucius.nutriprice.core.navigation.NavigationManager
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarController
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarEvent
import com.github.sarunasbucius.nutriprice.graphql.PreparedRecipeQuery
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

data class PreparedRecipeUiState(
    val recipeName: String = "",
    val preparedDate: String = "",
    val recipe: PreparedRecipeQuery.PreparedRecipe = PreparedRecipeQuery.PreparedRecipe(
        "",
        emptyList(),
        0.0,
        emptyList()
    ),
    val isLoading: Boolean = true
)

@HiltViewModel(assistedFactory = ViewPreparedRecipeViewModel.Factory::class)
class ViewPreparedRecipeViewModel @AssistedInject constructor(
    @Assisted val navKey: NutriPriceScreen.PreparedRecipe,
    private val apolloClient: ApolloClient,
    private val navigation: NavigationManager,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val recipeName: String = navKey.recipeName
    private val preparedDate: String = navKey.preparedDate

    var uiState by mutableStateOf(PreparedRecipeUiState())
        private set

    @AssistedFactory
    interface Factory {
        fun create(navKey: NutriPriceScreen.PreparedRecipe): ViewPreparedRecipeViewModel
    }

    init {
        fetchPreparedRecipe()
    }

    fun fetchPreparedRecipe() {
        viewModelScope.launch(ioDispatcher) {
            if (recipeName.isEmpty() || preparedDate.isEmpty()) {
                SnackbarController.sendEvent(SnackbarEvent("ERROR: something went wrong"))
                navigation.navigateUp()
                return@launch
            }
            uiState = uiState.copy(
                recipeName = recipeName,
                preparedDate = preparedDate,
                isLoading = true
            )
            val response =
                apolloClient.query(PreparedRecipeQuery(recipeName, preparedDate)).execute()
            uiState = uiState.copy(
                recipe = response.data?.preparedRecipe ?: PreparedRecipeQuery.PreparedRecipe(
                    "",
                    emptyList(),
                    0.0,
                    emptyList()
                ),
                isLoading = false
            )
            if (!response.errors.isNullOrEmpty()) {
                Log.e("ViewPreparedRecipeViewModel", response.errors.toString())
                SnackbarController.sendEvent(SnackbarEvent("ERROR: something went wrong"))
                navigation.navigateUp()
            }
        }
    }
}