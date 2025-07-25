package com.github.sarunasbucius.nutriprice.feature.recipe.viewRecipe

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
import com.github.sarunasbucius.nutriprice.graphql.RecipeQuery
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

data class RecipeUi(
    val recipe: RecipeQuery.Recipe = RecipeQuery.Recipe(false, "", emptyList(), emptyList()),
    val recipeName: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel(assistedFactory = RecipeViewModel.Factory::class)
class RecipeViewModel @AssistedInject constructor(
    @Assisted val navKey: NutriPriceScreen.Recipe,
    private val apolloClient: ApolloClient,
    private val navigation: NavigationManager,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val recipeName: String = navKey.recipeName

    var uiState by mutableStateOf(RecipeUi())
        private set

    @AssistedFactory
    interface Factory {
        fun create(navKey: NutriPriceScreen.Recipe): RecipeViewModel
    }

    init {
        fetchRecipe()
    }

    fun fetchRecipe() {
        viewModelScope.launch(ioDispatcher) {
            uiState = uiState.copy(
                recipeName = recipeName,
                isLoading = true
            )
            val response = apolloClient.query(RecipeQuery(recipeName)).execute()
            uiState = uiState.copy(
                recipe = response.data?.recipe ?: RecipeQuery.Recipe(
                    false,
                    "",
                    emptyList(),
                    emptyList()
                ),
                isLoading = false
            )
            if (!response.errors.isNullOrEmpty()) {
                Log.e("RecipeViewModel", response.errors.toString())
                SnackbarController.sendEvent(SnackbarEvent("ERROR: something went wrong"))
                navigation.navigateUp()
            }
        }
    }
}