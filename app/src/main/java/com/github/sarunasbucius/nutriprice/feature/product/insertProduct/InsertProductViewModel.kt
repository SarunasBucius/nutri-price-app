package com.github.sarunasbucius.nutriprice.feature.product.insertProduct

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.feature.product.common.model.NutritionalValueUi
import com.github.sarunasbucius.nutriprice.feature.product.common.model.PurchaseDetailsUi
import com.github.sarunasbucius.nutriprice.graphql.CreateProductMutation
import com.github.sarunasbucius.nutriprice.graphql.type.ProductAggregateInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InsertProductUiState(
    val productName: String = "",
    val varietyName: String = "",
    val purchaseDetails: PurchaseDetailsUi = PurchaseDetailsUi(),
    val nutritionalValues: NutritionalValueUi = NutritionalValueUi(),
    val errors: List<String> = emptyList(),
) {
    fun toApiModel(): ProductAggregateInput {
        val purchase = if (purchaseDetails.isEmpty()) {
            Optional.absent()
        } else {
            Optional.present(purchaseDetails.toApiModel())
        }

        val nutritionalValue = if (nutritionalValues.isEmpty()) {
            Optional.absent()
        } else {
            Optional.present(nutritionalValues.toApiModel())
        }
        return ProductAggregateInput(
            name = productName,
            varietyName = varietyName,
            purchase = purchase,
            nutritionalValue = nutritionalValue,
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
    private val apolloClient: ApolloClient,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    var uiState by mutableStateOf(InsertProductUiState())
        private set

    fun updateName(name: String) {
        uiState = uiState.copy(productName = name)
    }

    fun updateVarietyName(varietyName: String) {
        uiState = uiState.copy(varietyName = varietyName)
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
            val result = apolloClient.mutation(
                CreateProductMutation(input = uiState.toApiModel())
            ).execute()
            if (result.hasErrors()) {
                uiState = uiState.copy(errors = listOf("Something went wrong"))
                return@launch
            }
            onProductInserted()
        }
    }
}