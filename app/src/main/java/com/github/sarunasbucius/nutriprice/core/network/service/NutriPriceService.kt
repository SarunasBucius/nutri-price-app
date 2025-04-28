package com.github.sarunasbucius.nutriprice.core.network.service

import com.github.sarunasbucius.nutriprice.core.model.Recipe
import com.github.sarunasbucius.nutriprice.core.model.RecipeIdAndName
import com.github.sarunasbucius.nutriprice.core.network.model.SuccessResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface NutriPriceService {
    @GET("/recipes/names")
    suspend fun fetchRecipeList(): ApiResponse<List<RecipeIdAndName>>

    @POST("/recipes")
    suspend fun insertRecipe(@Body recipe: Recipe): ApiResponse<SuccessResponse>

    @GET("/recipes/{recipeId}")
    suspend fun fetchRecipe(@Path("recipeId") recipeId: String): ApiResponse<Recipe>
}