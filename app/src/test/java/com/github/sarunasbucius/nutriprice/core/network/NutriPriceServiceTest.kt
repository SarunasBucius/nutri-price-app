package com.github.sarunasbucius.nutriprice.core.network

import com.github.sarunasbucius.nutriprice.core.model.NutritionalValue
import com.github.sarunasbucius.nutriprice.core.model.Product
import com.github.sarunasbucius.nutriprice.core.network.service.NutriPriceService
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class NutriPriceServiceTest : ApiAbstract<NutriPriceService>() {

    private lateinit var service: NutriPriceService

    @Before
    fun initService() {
        service = createService(NutriPriceService::class.java)
    }

    @Test
    fun fetchProductListTest() = runTest {
        enqueueResponse("""["Apple", "Banana", "Carrot"]""")
        val response = service.fetchProductList()
        val responseBody = requireNotNull((response as ApiResponse.Success).data)

        assertThat(responseBody, `is`(listOf("Apple", "Banana", "Carrot")))
    }

    @Test
    fun insertProductTest() = runTest {
        enqueueResponse("""{"message": "Product inserted successfully"}""")
        val response = service.insertProduct(
            Product(
                name = "Apple",
                price = null,
                amount = null,
                unit = "",
                notes = "",
                nutritionalValues = NutritionalValue(
                    unit = "",
                    energyValueKcal = null,
                    fat = null,
                    saturatedFat = null,
                    carbohydrate = null,
                    carbohydrateSugars = null,
                    fibre = null,
                    protein = null,
                    salt = null,
                ),
            )
        )
        val responseBody = requireNotNull((response as ApiResponse.Success).data)

        assertThat(responseBody.message, `is`("Product inserted successfully"))
    }
}