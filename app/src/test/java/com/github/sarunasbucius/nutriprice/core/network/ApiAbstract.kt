package com.github.sarunasbucius.nutriprice.core.network

import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@RunWith(JUnit4::class)
abstract class ApiAbstract<T> {

    lateinit var mockWebServer: MockWebServer

    @Before
    fun mockServer() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun stopServer() {
        mockWebServer.shutdown()
    }

    fun enqueueResponse(responseBody: String) {
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody(responseBody))
    }

    fun createService(service: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(
                ApiResponseCallAdapterFactory.create(),
            )
            .build()
            .create(service)
    }
}