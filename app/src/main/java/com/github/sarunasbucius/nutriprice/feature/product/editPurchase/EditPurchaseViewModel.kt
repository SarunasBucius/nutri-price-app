package com.github.sarunasbucius.nutriprice.feature.product.editPurchase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.github.sarunasbucius.nutriprice.core.navigation.NavigationManager
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.core.navigation.model.PurchaseDetailsNav
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarController
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarEvent
import com.github.sarunasbucius.nutriprice.feature.product.common.model.PurchaseDetailsUi
import com.github.sarunasbucius.nutriprice.graphql.UpdatePurchaseMutation
import com.github.sarunasbucius.nutriprice.graphql.type.PurchaseInput
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

data class EditPurchaseUiState(
    val id: String = "",
    val purchase: PurchaseDetailsUi = PurchaseDetailsUi(),
    val errors: List<String> = listOf(),
)

@HiltViewModel(assistedFactory = EditPurchaseViewModel.Factory::class)
class EditPurchaseViewModel @AssistedInject constructor(
    @Assisted val navKey: NutriPriceScreen.EditPurchase,
    private val apolloClient: ApolloClient,
    private val navigation: NavigationManager,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    val purchaseDetails: PurchaseDetailsNav = navKey.purchaseDetails
    var uiState by mutableStateOf(EditPurchaseUiState())
        private set

    @AssistedFactory
    interface Factory {
        fun create(navKey: NutriPriceScreen.EditPurchase): EditPurchaseViewModel
    }

    init {
        updatePurchase(PurchaseDetailsUi.fromApiModel(purchaseDetails))
        uiState = uiState.copy(id = purchaseDetails.id)
    }

    fun updatePurchase(purchaseDetails: PurchaseDetailsUi) {
        uiState = uiState.copy(purchase = purchaseDetails)
    }

    fun submit() {
        val errors = mutableListOf<String>()
        errors.addAll(uiState.purchase.validate())
        uiState = uiState.copy(errors = errors)

        if (errors.isNotEmpty()) {
            return
        }
        viewModelScope.launch(ioDispatcher) {
            val result = apolloClient.mutation(
                UpdatePurchaseMutation(
                    id = uiState.id,
                    purchaseInput = PurchaseInput(
                        date = uiState.purchase.date,
                        retailer = uiState.purchase.retailer,
                        price = uiState.purchase.price.toDoubleOrNull() ?: 0.0,
                        quantity = uiState.purchase.amount.toDoubleOrNull() ?: 0.0,
                        unit = uiState.purchase.unit,
                        notes = uiState.purchase.notes
                    )
                )
            ).execute()
            if (result.hasErrors()) {
                errors.add("Failed to update purchase")
                uiState = uiState.copy(errors = errors)
            } else {
                SnackbarController.sendEvent(SnackbarEvent("Updated successfully"))
                navigation.navigateUp()
            }
        }
    }
}