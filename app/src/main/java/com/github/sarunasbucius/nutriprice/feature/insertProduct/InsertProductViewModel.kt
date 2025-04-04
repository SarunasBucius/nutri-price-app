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
import com.github.sarunasbucius.nutriprice.feature.common.model.PurchaseDetailsUi
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InsertProductUiState(
    val productName: String = "",
    val purchaseDetails: PurchaseDetailsUi = PurchaseDetailsUi(),
    val nutritionalValues: NutritionalValueUi = NutritionalValueUi(),
    val errors: List<String> = emptyList(),
) {
    fun toApiModel(): NewProduct {
        return NewProduct(
            name = productName,
            purchaseDetails = purchaseDetails.toApiModel(),
            nutritionalValues = nutritionalValues.toApiModel(),
        )
    }

    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (productName.isEmpty()) {
            errors.add("Name cannot be empty")
        }

        errors.addAll(nutritionalValues.validate())
        errors.addAll(purchaseDetails.validate())

        return errors
    }
}

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

    fun updatePurchaseDetails(purchasedProduct: PurchaseDetailsUi) {
        uiState = uiState.copy(purchaseDetails = purchasedProduct)
    }

    fun updateNutritionalValue(nutritionalValue: NutritionalValueUi) {
        uiState = uiState.copy(nutritionalValues = nutritionalValue)
    }

    fun insertProduct(onProductInserted: () -> Unit) {
        val errors = uiState.validate()
        uiState = uiState.copy(errors = errors)
        if (errors.isNotEmpty()) {
            return
        }

        viewModelScope.launch(ioDispatcher) {
            val result = nutriPriceClient.insertProduct(uiState.toApiModel())
            result.onSuccess {
                onProductInserted()
            }.onError {
                uiState = uiState.copy(errors = listOf("Something went wrong"))
            }
        }
    }
}