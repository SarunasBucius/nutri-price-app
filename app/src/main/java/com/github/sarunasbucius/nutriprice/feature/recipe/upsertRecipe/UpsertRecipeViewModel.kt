package com.github.sarunasbucius.nutriprice.feature.recipe.upsertRecipe

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
import com.github.sarunasbucius.nutriprice.feature.recipe.common.model.IngredientUi
import com.github.sarunasbucius.nutriprice.graphql.ProductsQuery
import com.github.sarunasbucius.nutriprice.graphql.RecipeQuery
import com.github.sarunasbucius.nutriprice.graphql.UpdateRecipeMutation
import com.github.sarunasbucius.nutriprice.graphql.type.IngredientInput
import com.github.sarunasbucius.nutriprice.graphql.type.RecipeInput
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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

data class UpsertRecipeUiState(
    val recipeName: String = "",
    val notes: String = "",
    val steps: List<String> = listOf(""),
    val ingredients: List<IngredientUi> = listOf(IngredientUi()),
    val errors: List<String> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel(assistedFactory = UpsertRecipeViewModel.Factory::class)
class UpsertRecipeViewModel @AssistedInject constructor(
    @Assisted val navKey: NutriPriceScreen.UpsertRecipe,
    private val navigation: NavigationManager,
    private val apolloClient: ApolloClient,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    val recipeName: String? = navKey.recipeName

    var uiState by mutableStateOf(UpsertRecipeUiState())
        private set

    @AssistedFactory
    interface Factory {
        fun create(navKey: NutriPriceScreen.UpsertRecipe): UpsertRecipeViewModel
    }

    val productList: StateFlow<List<String>> = flow {
        val response = apolloClient.query(ProductsQuery()).execute()
        emit(response.data?.products?.map { it.name } ?: emptyList())

        if (!response.errors.isNullOrEmpty()) {
            Log.e("UpsertRecipeViewModel", response.errors.toString())
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
        if (recipeName != null) {
            viewModelScope.launch(ioDispatcher) {
                val result = apolloClient.query(RecipeQuery(recipeName)).execute()
                if (result.hasErrors()) {
                    SnackbarController.sendEvent(SnackbarEvent("ERROR: failed to fetch recipe"))
                    navigation.navigateUp()
                    return@launch
                }

                val steps: MutableList<String> =
                    result.data?.recipe?.steps?.toMutableList() ?: mutableListOf()
                steps.add("")

                val ingredients: MutableList<IngredientUi> = result.data?.recipe?.ingredients?.map {
                    IngredientUi(
                        name = it.product,
                        amount = it.quantity.toString(),
                        unit = it.unit,
                        notes = it.notes
                    )
                }?.toMutableList() ?: mutableListOf()
                ingredients.add(IngredientUi())

                uiState = uiState.copy(
                    recipeName = recipeName,
                    notes = result.data?.recipe?.notes ?: "",
                    steps = steps,
                    ingredients = ingredients,
                    isLoading = false
                )
            }
        } else {
            uiState = uiState.copy(isLoading = false)
        }
    }

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

    fun upsertRecipe(onRecipeUpsert: () -> Unit) {
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

        val steps = uiState.steps.filter { it.isNotEmpty() }

        viewModelScope.launch(ioDispatcher) {
            val result = apolloClient.mutation(
                UpdateRecipeMutation(
                    recipe = RecipeInput(
                        recipeName = uiState.recipeName,
                        steps = steps,
                        notes = uiState.notes,
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
            } else {
                onRecipeUpsert()
            }
        }
    }
}