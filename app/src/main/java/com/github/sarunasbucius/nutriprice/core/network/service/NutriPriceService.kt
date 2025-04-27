package com.github.sarunasbucius.nutriprice.core.network.service

import com.github.sarunasbucius.nutriprice.core.model.NewProduct
import com.github.sarunasbucius.nutriprice.core.model.Product
import com.github.sarunasbucius.nutriprice.core.model.Recipe
import com.github.sarunasbucius.nutriprice.core.network.model.SuccessResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface NutriPriceService {

    @GET("products")
    suspend fun fetchProductList(): ApiResponse<List<String>>

    @POST("products")
    suspend fun insertProduct(@Body newProduct: NewProduct): ApiResponse<SuccessResponse>

    @GET("/recipes/names")
    suspend fun fetchRecipeList(): ApiResponse<List<String>>

    @POST("/recipes")
    suspend fun insertRecipe(@Body recipe: Recipe): ApiResponse<SuccessResponse>

    @GET("products/{productName}")
    suspend fun fetchProduct(@Path("productName") productName: String): ApiResponse<Product>

    @PUT("products/{productName}")
    suspend fun updateProduct(
        @Path("productName") productName: String,
        @Body product: Product
    ): ApiResponse<SuccessResponse>

    @DELETE("products/{productName}")
    suspend fun deleteProduct(@Path("productName") productName: String): ApiResponse<SuccessResponse>
}