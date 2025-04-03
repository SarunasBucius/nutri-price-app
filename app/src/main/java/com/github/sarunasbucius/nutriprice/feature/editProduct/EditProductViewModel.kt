package com.github.sarunasbucius.nutriprice.feature.editProduct

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sarunasbucius.nutriprice.core.model.Product
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.network.service.NutriPriceClient
import com.github.sarunasbucius.nutriprice.feature.common.model.NutritionalValueUi
import com.github.sarunasbucius.nutriprice.feature.common.model.PurchasedProduct
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProductUiState(
    val productName: String = "",
    val purchasedProducts: SnapshotStateList<PurchasedProduct> = mutableStateListOf(),
    val nutritionalValues: NutritionalValueUi = NutritionalValueUi(),
    val errors: SnapshotStateList<String> = mutableStateListOf(),
) {
    fun toApiModel(): Product {
        return Product(
            name = productName,
            purchases = purchasedProducts.map { it.toApiModel() },
            nutritionalValues = nutritionalValues.toApiModel()
        )
    }

    companion object {
        fun fromApiModel(product: Product): EditProductUiState {
            return EditProductUiState(
                productName = product.name,
                purchasedProducts = product.purchases.map {
                    PurchasedProduct.fromApiModel(it)
                }.toMutableStateList(),
                nutritionalValues = NutritionalValueUi.fromApiModel(product.nutritionalValues)
            )
        }
    }
}


@HiltViewModel
class EditProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val nutriPriceClient: NutriPriceClient,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val productName: String = savedStateHandle["productName"] ?: ""

    var uiState by mutableStateOf(EditProductUiState(productName = productName))
        private set

    fun fetchProduct() {
        viewModelScope.launch(ioDispatcher) {
            val result = nutriPriceClient.fetchProduct(productName)
            result.onSuccess {
                uiState = EditProductUiState.fromApiModel(data)
            }.onFailure {
                Log.e("EditProductViewModel", "fetch product: ${message()}")
                uiState.errors.add("Something went wrong while fetching product")
            }
        }
    }

    fun updateProductName(name: String) {
        uiState = uiState.copy(productName = name)
    }

    fun updatePurchaseDetails(index: Int, purchase: PurchasedProduct) {
        if (index < 0 || index >= uiState.purchasedProducts.size) {
            Log.e("EditProductViewModel", "Invalid index: $index")
            return
        }

        uiState.purchasedProducts[index] = purchase
    }

    fun updateNutritionalValue(nutritionalValue: NutritionalValueUi) {
        uiState = uiState.copy(nutritionalValues = nutritionalValue)
    }

    fun deleteProduct(onProductDeleted: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            val result = nutriPriceClient.deleteProduct(productName)
            result.onSuccess {
                onProductDeleted()
            }.onFailure {
                Log.e("EditProductViewModel", "delete product: ${message()}")
                uiState = uiState.copy(errors = mutableStateListOf("Something went wrong"))
            }
        }
    }

    fun insertProduct(onProductInserted: () -> Unit) {
        val errors = mutableStateListOf<String>()
        if (uiState.productName.isEmpty()) {
            errors.add("Name cannot be empty")
        }

        errors.addAll(uiState.nutritionalValues.validate())

        for (product in uiState.purchasedProducts) {
            errors.addAll(product.validate())
        }

        uiState = uiState.copy(errors = errors)
        if (errors.isNotEmpty()) {
            return
        }

        viewModelScope.launch(ioDispatcher) {
            val result = nutriPriceClient.updateProduct(uiState.toApiModel())
            result.onSuccess {
                onProductInserted()
            }.onFailure {
                uiState = uiState.copy(errors = mutableStateListOf("Something went wrong"))
            }
        }
    }
}