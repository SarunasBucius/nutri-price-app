package com.github.sarunasbucius.nutriprice.feature.recipe.planRecipes

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
import com.github.sarunasbucius.nutriprice.graphql.PlanRecipesMutation
import com.github.sarunasbucius.nutriprice.graphql.RecipesQuery
import com.github.sarunasbucius.nutriprice.graphql.type.PlanRecipe
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PlanRecipesUiState(
    val date: String = "",
    val plannedRecipes: List<PlanRecipeUi> = listOf(PlanRecipeUi()),
    val errors: List<String> = emptyList()
)

data class PlanRecipeUi(
    val recipeName: String = "",
    val portion: String = "1",
)

@HiltViewModel(assistedFactory = PlanRecipesViewModel.Factory::class)
class PlanRecipesViewModel @AssistedInject constructor(
    @Assisted val navKey: NutriPriceScreen.PlanRecipes,
    private val apolloClient: ApolloClient,
    private val navigation: NavigationManager,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    val date: String = navKey.date
    var uiState by mutableStateOf(PlanRecipesUiState(date = date))
        private set

    @AssistedFactory
    interface Factory {
        fun create(navKey: NutriPriceScreen.PlanRecipes): PlanRecipesViewModel
    }

    val recipeList: StateFlow<List<String>> = flow {
        val response = apolloClient.query(RecipesQuery()).execute()
        emit(response.data?.recipes ?: emptyList())

        if (!response.errors.isNullOrEmpty()) {
            Log.e("PlanRecipesViewModel", response.errors.toString())
            SnackbarController.sendEvent(SnackbarEvent("ERROR: something went wrong"))
            navigation.navigateUp()
        }
    }.flowOn(ioDispatcher).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun updateDate(date: String) {
        uiState = uiState.copy(date = date)
    }

    fun updatePortion(portion: String, index: Int) {
        val recipes = uiState.plannedRecipes.toMutableList()
        recipes[index] = recipes[index].copy(portion = portion)
        val lastRecipe = recipes.last()
        if (lastRecipe.recipeName.isNotEmpty()) {
            recipes.add(PlanRecipeUi())
        }
        uiState = uiState.copy(plannedRecipes = recipes)
    }

    fun updateRecipeName(recipeName: String, index: Int) {
        val recipes = uiState.plannedRecipes.toMutableList()
        recipes[index] = recipes[index].copy(recipeName = recipeName)
        val lastRecipe = recipes.last()
        if (lastRecipe.recipeName.isNotEmpty()) {
            recipes.add(PlanRecipeUi())
        }
        uiState = uiState.copy(plannedRecipes = recipes)
    }

    fun submit() {
        val errors = mutableListOf<String>()
        if (uiState.date.isEmpty()) {
            errors.add("Date cannot be empty")
        }
        val recipes = uiState.plannedRecipes.filter { it.recipeName.isNotEmpty() }
        if (recipes.isEmpty()) {
            errors.add("At least one recipe must be selected")
        }
        for (recipe in recipes) {
            if (recipe.portion.toDoubleOrNull() == null) {
                errors.add("Specify portions")
                break
            }
        }

        uiState = uiState.copy(errors = errors)
        if (errors.isNotEmpty()) {
            return
        }

        viewModelScope.launch(ioDispatcher) {
            val result = apolloClient.mutation(
                PlanRecipesMutation(
                    date = uiState.date,
                    planRecipes = recipes.map {
                        PlanRecipe(
                            it.recipeName,
                            it.portion.toDouble()
                        )
                    }
                )).execute()
            if (result.hasErrors()) {
                Log.e("PlanRecipesViewModel", result.errors.toString())
                errors.add("Something went wrong")
                uiState = uiState.copy(errors = errors)
            } else {
                navigation.navigateUp()
            }
        }
    }
}