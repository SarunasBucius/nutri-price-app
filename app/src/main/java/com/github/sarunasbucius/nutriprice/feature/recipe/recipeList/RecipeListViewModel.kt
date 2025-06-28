package com.github.sarunasbucius.nutriprice.feature.recipe.recipeList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.github.sarunasbucius.nutriprice.core.navigation.NavigationManager
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeListUiState(
    val isLoading: Boolean = true,
    val searchInput: String = "",
)

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val apolloClient: ApolloClient,
    private val navigation: NavigationManager,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<RecipeListUiState>(RecipeListUiState())
    internal val uiState = _uiState.asStateFlow()

    private val _recipeList = MutableStateFlow<List<String>>(emptyList())

    val filteredRecipeList: StateFlow<List<String>> =
        uiState
            .map { state ->
                val input = state.searchInput.trim()
                if (input.isBlank()) {
                    _recipeList.value
                } else {
                    _recipeList.value.filter {
                        it.contains(input, ignoreCase = true)
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    init {
        fetchRecipes()
    }

    fun fetchRecipes() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            val response = apolloClient.query(RecipesQuery()).execute()
            if (!response.errors.isNullOrEmpty()) {
                SnackbarController.sendEvent(SnackbarEvent("ERROR: something went wrong"))
                navigation.navigateUp()
            } else {
                _recipeList.update { response.data?.recipes ?: emptyList() }
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onSearchInputChanged(newInput: String) {
        _uiState.update { it.copy(searchInput = newInput) }
    }
}