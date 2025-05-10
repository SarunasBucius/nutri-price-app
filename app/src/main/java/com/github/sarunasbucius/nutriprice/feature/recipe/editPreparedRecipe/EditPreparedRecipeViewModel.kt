package com.github.sarunasbucius.nutriprice.feature.recipe.editPreparedRecipe

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.github.sarunasbucius.nutriprice.core.navigation.AppComposeNavigator
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.core.navigation.model.PreparedRecipeNav
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarController
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarEvent
import com.github.sarunasbucius.nutriprice.feature.recipe.common.model.IngredientUi
import com.github.sarunasbucius.nutriprice.graphql.ProductsQuery
import com.github.sarunasbucius.nutriprice.graphql.UpdatePreparedRecipeMutation
import com.github.sarunasbucius.nutriprice.graphql.type.IngredientInput
import com.github.sarunasbucius.nutriprice.graphql.type.PreparedRecipeInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

data class EditPreparedRecipeUiState(
    val recipeName: String = "",
    val notes: String = "",
    val steps: List<String> = listOf(""),
    val ingredients: List<IngredientUi> = listOf(IngredientUi()),
    val preparedDate: String = "",
    val portion: String = "",
    val errors: List<String> = emptyList(),
    val isLoading: Boolean = true,
    val portionNumber: Double = 1.0
)

@HiltViewModel
class EditPreparedRecipeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val apolloClient: ApolloClient,
    private val navigation: AppComposeNavigator<NutriPriceScreen>,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    val preparedRecipe: PreparedRecipeNav? = savedStateHandle["preparedRecipe"]

    var uiState by mutableStateOf(EditPreparedRecipeUiState())
        private set

    val productList: StateFlow<List<String>> = flow {
        val response = apolloClient.query(ProductsQuery()).execute()
        emit(response.data?.products?.map { it.name } ?: emptyList())

        if (!response.errors.isNullOrEmpty()) {
            Log.e("EditPreparedRecipeViewModel", response.errors.toString())
            SnackbarController.sendEvent(SnackbarEvent("ERROR: failed to fetch products"))
        }
    }.onStart { uiState = uiState.copy(isLoading = true) }
        .onCompletion { uiState = uiState.copy(isLoading = false) }
        .flowOn(ioDispatcher).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        if (preparedRecipe == null) {
            viewModelScope.launch {
                SnackbarController.sendEvent(SnackbarEvent("ERROR: something went wrong"))
                navigation.navigateUp()
            }
        } else {
            uiState = uiState.copy(
                recipeName = preparedRecipe.name,
                notes = preparedRecipe.notes,
                steps = preparedRecipe.steps,
                ingredients = preparedRecipe.ingredients.map {
                    IngredientUi(
                        it.product,
                        it.amount.toString(),
                        it.unit,
                        it.notes
                    )
                } + IngredientUi(),
                preparedDate = preparedRecipe.preparedDate,
                portion = preparedRecipe.portion.toString(),
                isLoading = false,
                portionNumber = preparedRecipe.portion
            )
        }
    }

    fun updatePreparedDate(date: String) {
        uiState = uiState.copy(preparedDate = date)
    }

    fun updatePortion(portions: String) {
        uiState = uiState.copy(portion = portions)
        val portionsNumber = portions.toDoubleOrNull()
        if (portionsNumber != null) {
            uiState = uiState.copy(portionNumber = portionsNumber)
        }
    }

    fun updateIngredient(ingredient: IngredientUi, index: Int) {
        val ingredients = uiState.ingredients.toMutableList()
        ingredients[index] = ingredient
        if (ingredients.last().name.isNotEmpty()) {
            ingredients.add(IngredientUi())
        }
        uiState = uiState.copy(ingredients = ingredients)
    }

    fun removeIngredient(index: Int) {
        val ingredients = uiState.ingredients.toMutableList()
        ingredients.removeAt(index)
        uiState = uiState.copy(ingredients = ingredients)
    }

    fun multiplyIngredientAmountByPortion(ingredientAmount: String): String {
        val amount = ingredientAmount.toDoubleOrNull() ?: return ""
        val multiplied = amount * uiState.portionNumber
        val formatter = DecimalFormat("0.##")
        return formatter.format(multiplied)
    }

    fun submitPreparedRecipe() {
        val errors = mutableListOf<String>()

        val ingredients = uiState.ingredients.filter { it.name.isNotEmpty() }
        for (ingredient in ingredients) {
            if (ingredient.amount.isNotEmpty() && ingredient.amount.toDoubleOrNull() == null) {
                errors.add("Ingredient amount must be a number")
                break
            }
        }

        if (uiState.portion.toDoubleOrNull() == null) {
            errors.add("Portion must be a number")
        }

        uiState = uiState.copy(errors = errors)
        if (errors.isNotEmpty()) {
            return
        }

        viewModelScope.launch(ioDispatcher) {
            val result = apolloClient.mutation(
                UpdatePreparedRecipeMutation(
                    recipe = PreparedRecipeInput(
                        recipeName = uiState.recipeName,
                        steps = uiState.steps,
                        notes = uiState.notes,
                        preparedDate = uiState.preparedDate,
                        portion = uiState.portionNumber,
                        ingredients = ingredients.map { ingredient ->
                            IngredientInput(
                                product = ingredient.name,
                                quantity = ingredient.amount.toDoubleOrNull() ?: 0.0,
                                unit = ingredient.unit,
                                notes = ingredient.notes,
                            )
                        }
                    ))).execute()
            if (result.hasErrors()) {
                uiState = uiState.copy(errors = listOf("Something went wrong"))
                return@launch
            }
            navigation.navigateUp()
        }
    }
}