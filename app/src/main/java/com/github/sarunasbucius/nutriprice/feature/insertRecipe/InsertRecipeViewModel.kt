package com.github.sarunasbucius.nutriprice.feature.insertRecipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sarunasbucius.nutriprice.core.model.Ingredient
import com.github.sarunasbucius.nutriprice.core.model.QuantityUnit
import com.github.sarunasbucius.nutriprice.core.model.Recipe
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.network.service.NutriPriceClient
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InsertRecipeUiState(
    val recipeName: String = "",
    val notes: String = "",
    val steps: List<String> = listOf(""),
    val ingredients: List<IngredientUi> = listOf(IngredientUi()),
    val errors: List<String> = emptyList(),
)

data class IngredientUi(
    val name: String = "",
    val amount: String = "",
    val unit: QuantityUnit = QuantityUnit.UNSPECIFIED,
    val notes: String = "",
)

@HiltViewModel
class InsertRecipeViewModel @Inject constructor(
    private val nutriPriceClient: NutriPriceClient,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    var uiState by mutableStateOf(InsertRecipeUiState())
        private set

    fun updateName(name: String) {
        uiState = uiState.copy(recipeName = name)
    }

    fun updateNotes(notes: String) {
        uiState = uiState.copy(notes = notes)
    }

    fun updateStep(step: String, index: Int) {
        val steps = uiState.steps.toMutableList()
        steps[index] = step
        if (steps.last().isNotEmpty()) {
            steps.add("")
        }
        uiState = uiState.copy(steps = steps)
    }

    fun updateIngredient(ingredient: IngredientUi, index: Int) {
        val ingredients = uiState.ingredients.toMutableList()
        ingredients[index] = ingredient
        if (ingredients.last().name.isNotEmpty()) {
            ingredients.add(IngredientUi())
        }
        uiState = uiState.copy(ingredients = ingredients)
    }

    fun removeStep(index: Int) {
        val steps = uiState.steps.toMutableList()
        steps.removeAt(index)
        uiState = uiState.copy(steps = steps)
    }

    fun removeIngredient(index: Int) {
        val ingredients = uiState.ingredients.toMutableList()
        ingredients.removeAt(index)
        uiState = uiState.copy(ingredients = ingredients)
    }

    fun insertRecipe(onRecipeInserted: () -> Unit) {
        val errors = mutableListOf<String>()
        if (uiState.recipeName.isEmpty()) {
            errors.add("Name cannot be empty")
        }

        val ingredients = uiState.ingredients.filter { it.name.isNotEmpty() }

        for (ingredient in ingredients) {
            if (ingredient.amount.isNotEmpty() && ingredient.amount.toDoubleOrNull() == null) {
                errors.add("Ingredient amount must be a number")
                break
            }
        }

        uiState = uiState.copy(errors = errors)
        if (errors.isNotEmpty()) {
            return
        }

        val recipe = Recipe(
            name = uiState.recipeName,
            notes = uiState.notes,
            steps = uiState.steps.filter { it.isNotEmpty() },
            ingredients = ingredients.map { ingredient ->
                Ingredient(
                    name = ingredient.name,
                    amount = ingredient.amount.toDoubleOrNull(),
                    unit = ingredient.unit,
                    notes = ingredient.notes,
                )
            }
        )

        viewModelScope.launch(ioDispatcher) {
            val result = nutriPriceClient.insertRecipe(recipe)
            result.onSuccess {
                onRecipeInserted()
            }.onError {
                uiState = uiState.copy(errors = listOf("Something went wrong"))
            }
        }
    }
}