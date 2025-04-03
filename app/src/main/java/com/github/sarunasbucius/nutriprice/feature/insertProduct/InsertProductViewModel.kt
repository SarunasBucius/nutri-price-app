package com.github.sarunasbucius.nutriprice.feature.insertProduct

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sarunasbucius.nutriprice.core.model.NewProduct
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.network.service.NutriPriceClient
import com.github.sarunasbucius.nutriprice.feature.common.model.NutritionalValueUi
import com.github.sarunasbucius.nutriprice.feature.common.model.PurchasedProduct
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InsertProductUiState(
    val productName: String = "",
    val purchaseDetails: PurchasedProduct = PurchasedProduct(),
    val nutritionalValues: NutritionalValueUi = NutritionalValueUi(),
    val errors: List<String> = emptyList(),
)

@HiltViewModel
class InsertProductViewModel @Inject constructor(
    private val nutriPriceClient: NutriPriceClient,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    var uiState by mutableStateOf(InsertProductUiState())
        private set

    fun updateName(name: String) {
        uiState = uiState.copy(productName = name)
    }

    fun updatePurchaseDetails(purchasedProduct: PurchasedProduct) {
        uiState = uiState.copy(purchaseDetails = purchasedProduct)
    }

    fun updateNutritionalValue(nutritionalValue: NutritionalValueUi) {
        uiState = uiState.copy(nutritionalValues = nutritionalValue)
    }

    fun insertProduct(onProductInserted: () -> Unit) {
        val errors = mutableListOf<String>()
        if (uiState.productName.isEmpty()) {
            errors.add("Name cannot be empty")
        }

        fun validateNumericField(value: String, fieldName: String) {
            if (value.isNotEmpty() && value.toDoubleOrNull() == null) {
                errors.add("$fieldName must be a number")
            }
        }

        errors.addAll(uiState.nutritionalValues.validate())

        validateNumericField(uiState.purchaseDetails.price, "Price")
        validateNumericField(uiState.purchaseDetails.amount, "Amount")

        uiState = uiState.copy(errors = errors)
        if (errors.isNotEmpty()) {
            return
        }

        val newProduct = NewProduct(
            name = uiState.productName,
            price = uiState.purchaseDetails.price.toDoubleOrNull(),
            amount = uiState.purchaseDetails.amount.toDoubleOrNull(),
            unit = uiState.purchaseDetails.unit,
            notes = uiState.purchaseDetails.notes,
            nutritionalValues = uiState.nutritionalValues.toApiModel(),
        )
        viewModelScope.launch(ioDispatcher) {
            val result = nutriPriceClient.insertProduct(newProduct)
            result.onSuccess {
                onProductInserted()
            }.onError {
                uiState = uiState.copy(errors = listOf("Something went wrong"))
            }
        }
    }
}