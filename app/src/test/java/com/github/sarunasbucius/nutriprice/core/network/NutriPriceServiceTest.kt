package com.github.sarunasbucius.nutriprice.core.network

import com.github.sarunasbucius.nutriprice.core.model.NewProduct
import com.github.sarunasbucius.nutriprice.core.model.NutritionalValue
import com.github.sarunasbucius.nutriprice.core.model.NutritionalValueUnit
import com.github.sarunasbucius.nutriprice.core.model.PurchaseDetails
import com.github.sarunasbucius.nutriprice.core.model.QuantityUnit
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
            NewProduct(
                name = "Apple",
                purchaseDetails = PurchaseDetails(
                    price = null,
                    amount = null,
                    unit = QuantityUnit.UNSPECIFIED,
                    notes = "",
                ),
                nutritionalValues = NutritionalValue(
                    unit = NutritionalValueUnit.UNSPECIFIED,
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