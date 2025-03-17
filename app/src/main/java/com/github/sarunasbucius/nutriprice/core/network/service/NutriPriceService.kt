package com.github.sarunasbucius.nutriprice.core.network.service

import com.github.sarunasbucius.nutriprice.core.model.Product
import com.github.sarunasbucius.nutriprice.core.network.model.SuccessResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface NutriPriceService {

    @GET("products")
    suspend fun fetchProductList(): ApiResponse<List<String>>

    @POST("products")
    suspend fun insertProduct(@Body product: Product): ApiResponse<SuccessResponse>
}