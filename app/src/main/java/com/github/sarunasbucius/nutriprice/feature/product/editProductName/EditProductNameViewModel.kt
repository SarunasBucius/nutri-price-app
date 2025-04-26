package com.github.sarunasbucius.nutriprice.feature.product.editProductName

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.github.sarunasbucius.nutriprice.core.navigation.AppComposeNavigator
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarController
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarEvent
import com.github.sarunasbucius.nutriprice.graphql.UpdateProductNameMutation
import com.github.sarunasbucius.nutriprice.graphql.UpdateVarietyNameMutation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProductNameUi(
    val productName: String,
    val varietyName: String,
    val errors: List<String> = emptyList()
)

@HiltViewModel
class EditProductNameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val apolloClient: ApolloClient,
    private val navigation: AppComposeNavigator<NutriPriceScreen>,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val productId: String =
        savedStateHandle["productId"] ?: ""
    private val productName: String =
        savedStateHandle["productName"] ?: ""
    private val varietyName: String =
        savedStateHandle["varietyName"] ?: ""

    var uiState by mutableStateOf(EditProductNameUi(productName, varietyName))
        private set

    init {
        if (productId.isEmpty()) {
            viewModelScope.launch {
                SnackbarController.sendEvent(SnackbarEvent("ERROR: something went wrong"))
                navigation.navigateUp()
            }
        }
    }

    fun updateProductName(productName: String) {
        uiState = uiState.copy(productName = productName)
    }

    fun updateVarietyName(varietyName: String) {
        uiState = uiState.copy(varietyName = varietyName)
    }

    fun submit() {
        val errors = mutableListOf<String>()
        if (uiState.productName.isEmpty()) {
            errors.add("Product name cannot be empty")
        }
        if (uiState.varietyName.isEmpty()) {
            errors.add("Variety name cannot be empty")
        }
        uiState = uiState.copy(errors = errors)
        if (!errors.isEmpty()) {
            return
        }

        viewModelScope.launch(ioDispatcher) {
            val updateProductName = async { updateProductName() }
            val updateVarietyName = async { updateVarietyName() }

            val productNameResult = updateProductName.await()
            if (productNameResult != null && productNameResult.hasErrors()) {
                errors.add("Failed to update product name")
            }
            val varietyNameResult = updateVarietyName.await()
            if (varietyNameResult != null && varietyNameResult.hasErrors()) {
                errors.add("Failed to update variety name")
            }

            if (!errors.isEmpty()) {
                uiState = uiState.copy(errors = errors)
            } else {
                SnackbarController.sendEvent(SnackbarEvent("Updated successfully"))
                navigation.navigateUp()
            }
        }
    }

    suspend fun updateProductName(): ApolloResponse<UpdateProductNameMutation.Data>? {
        if (uiState.productName == productName) {
            return null
        }
        return apolloClient.mutation(
            UpdateProductNameMutation(productId, uiState.productName)
        ).execute()
    }

    suspend fun updateVarietyName(): ApolloResponse<UpdateVarietyNameMutation.Data>? {
        if (uiState.varietyName == varietyName) {
            return null
        }
        return apolloClient.mutation(
            UpdateVarietyNameMutation(varietyName, uiState.varietyName)
        ).execute()
    }
}