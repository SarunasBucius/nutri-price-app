package com.github.sarunasbucius.nutriprice.feature.recipe.recipeList

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.github.sarunasbucius.nutriprice.core.navigation.AppComposeNavigator
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarController
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarEvent
import com.github.sarunasbucius.nutriprice.graphql.RecipesQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val apolloClient: ApolloClient,
    private val navigation: AppComposeNavigator<NutriPriceScreen>,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<RecipeListUiState>(RecipeListUiState.Idle)
    internal val uiState = _uiState.asStateFlow()

    val recipeList: StateFlow<List<String>> = flow {
        val response = apolloClient.query(RecipesQuery()).execute()
        emit(response.data?.recipes ?: emptyList())

        if (!response.errors.isNullOrEmpty()) {
            Log.e("RecipeListViewModel", response.errors.toString())
            SnackbarController.sendEvent(SnackbarEvent("ERROR: something went wrong"))
            navigation.navigateUp()
        }
    }.onStart { _uiState.emit(RecipeListUiState.Loading) }
        .onCompletion { _uiState.emit(RecipeListUiState.Idle) }.flowOn(ioDispatcher).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

}

@Stable
internal sealed interface RecipeListUiState {
    data object Idle : RecipeListUiState
    data object Loading : RecipeListUiState
    data class Error(val message: String) : RecipeListUiState
}