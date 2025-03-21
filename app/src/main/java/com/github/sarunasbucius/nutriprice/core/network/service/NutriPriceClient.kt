package com.github.sarunasbucius.nutriprice.core.network.service

import com.github.sarunasbucius.nutriprice.core.model.Product
import com.github.sarunasbucius.nutriprice.core.model.Recipe
import com.github.sarunasbucius.nutriprice.core.network.model.SuccessResponse
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class NutriPriceClient @Inject constructor(
    private val nutriPriceService: NutriPriceService,
) {
    suspend fun fetchProductList(): ApiResponse<List<String>> =
        nutriPriceService.fetchProductList()

    suspend fun insertProduct(product: Product): ApiResponse<SuccessResponse> =
        nutriPriceService.insertProduct(product)

    suspend fun fetchRecipeList(): ApiResponse<List<String>> =
        nutriPriceService.fetchRecipeList()

    suspend fun insertRecipe(recipe: Recipe): ApiResponse<SuccessResponse> =
        nutriPriceService.insertRecipe(recipe)
}