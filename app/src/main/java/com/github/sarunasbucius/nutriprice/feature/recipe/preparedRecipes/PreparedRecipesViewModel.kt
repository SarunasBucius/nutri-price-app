package com.github.sarunasbucius.nutriprice.feature.recipe.preparedRecipes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarController
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarEvent
import com.github.sarunasbucius.nutriprice.graphql.PreparedRecipesByDateQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

data class PreparedRecipesUiState(
    val date: String = "",
    val preparedRecipes: List<String> = emptyList()
)

@HiltViewModel
class PreparedRecipesViewModel @Inject constructor(
    private val apolloClient: ApolloClient,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow(PreparedRecipesUiState())
    internal val uiState = _uiState.asStateFlow()

    init {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        _uiState.value = _uiState.value.copy(date = today)
        viewModelScope.launch(ioDispatcher) {
            val result = apolloClient.query(PreparedRecipesByDateQuery(today)).execute()
            _uiState.value = _uiState.value.copy(
                preparedRecipes = result.data?.preparedRecipesByDate ?: emptyList()
            )
            if (result.errors != null) {
                Log.e("RecipeListViewModel", result.errors.toString())
                SnackbarController.sendEvent(SnackbarEvent("ERROR: failed to fetch prepared recipes"))
            }
        }
    }

    fun updateDate(date: String) {
        _uiState.value = _uiState.value.copy(date = date)
        if (isValidDate(date)) {
            viewModelScope.launch(ioDispatcher) {
                val result = apolloClient.query(PreparedRecipesByDateQuery(date)).execute()
                _uiState.value = _uiState.value.copy(
                    preparedRecipes = result.data?.preparedRecipesByDate ?: emptyList()
                )
                if (result.errors != null) {
                    Log.e("RecipeListViewModel", result.errors.toString())
                    SnackbarController.sendEvent(SnackbarEvent("ERROR: failed to fetch prepared recipes"))
                }
            }
        }
    }

    private fun isValidDate(dateStr: String): Boolean {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            LocalDate.parse(dateStr, formatter)
            true
        } catch (_: DateTimeParseException) {
            false
        }
    }
}

