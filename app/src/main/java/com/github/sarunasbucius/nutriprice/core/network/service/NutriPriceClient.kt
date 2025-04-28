package com.github.sarunasbucius.nutriprice.core.network.service

import com.github.sarunasbucius.nutriprice.core.model.Recipe
import com.github.sarunasbucius.nutriprice.core.model.RecipeIdAndName
import com.github.sarunasbucius.nutriprice.core.network.model.SuccessResponse
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class NutriPriceClient @Inject constructor(
    private val nutriPriceService: NutriPriceService,
) {
    suspend fun fetchRecipeList(): ApiResponse<List<RecipeIdAndName>> =
        nutriPriceService.fetchRecipeList()

    suspend fun insertRecipe(recipe: Recipe): ApiResponse<SuccessResponse> =
        nutriPriceService.insertRecipe(recipe)

    suspend fun fetchRecipe(recipeId: String): ApiResponse<Recipe> =
        nutriPriceService.fetchRecipe(recipeId)

}