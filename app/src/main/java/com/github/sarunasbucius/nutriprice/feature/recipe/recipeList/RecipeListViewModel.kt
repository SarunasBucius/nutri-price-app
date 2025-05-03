package com.github.sarunasbucius.nutriprice.feature.recipe.recipeList

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sarunasbucius.nutriprice.core.model.RecipeIdAndName
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.network.service.NutriPriceClient
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
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
    private val nutriPriceClient: NutriPriceClient,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<RecipeListUiState>(RecipeListUiState.Idle)
    internal val uiState = _uiState.asStateFlow()

    val recipeList: StateFlow<List<RecipeIdAndName>> = flow {
        nutriPriceClient.fetchRecipeList().suspendOnSuccess {
            emit(data)
        }.onFailure {
            Log.e("RecipeListViewModel", message())
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