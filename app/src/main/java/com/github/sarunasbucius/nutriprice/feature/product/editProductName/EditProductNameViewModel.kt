package com.github.sarunasbucius.nutriprice.feature.product.editProductName

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.github.sarunasbucius.nutriprice.core.navigation.NavigationManager
import com.github.sarunasbucius.nutriprice.core.navigation.NavigationResult
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarController
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarEvent
import com.github.sarunasbucius.nutriprice.graphql.UpdateProductNameMutation
import com.github.sarunasbucius.nutriprice.graphql.UpdateVarietyNameMutation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

data class EditProductNameUi(
    val productName: String,
    val varietyName: String,
    val errors: List<String> = emptyList()
)

@HiltViewModel(assistedFactory = EditProductNameViewModel.Factory::class)
class EditProductNameViewModel @AssistedInject constructor(
    @Assisted val navKey: NutriPriceScreen.EditProductName,
    private val apolloClient: ApolloClient,
    private val navigation: NavigationManager,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    var uiState by mutableStateOf(EditProductNameUi(navKey.productName, navKey.varietyName))
        private set

    @AssistedFactory
    interface Factory {
        fun create(navKey: NutriPriceScreen.EditProductName): EditProductNameViewModel
    }

    init {
        Log.d("EditProductNameViewModel", "Init EditProductNameViewModel $navKey")
        if (navKey.productId.isEmpty()) {
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
                navigation.sendResultAndNavigateUp(
                    result = NavigationResult.ProductName(
                        productId = navKey.productId,
                        productName = uiState.productName,
                        variety = uiState.varietyName,
                        oldVariety = navKey.varietyName
                    ),
                )
            }
        }
    }

    suspend fun updateProductName(): ApolloResponse<UpdateProductNameMutation.Data>? {
        if (uiState.productName == navKey.productName) {
            return null
        }
        return apolloClient.mutation(
            UpdateProductNameMutation(navKey.productId, uiState.productName)
        ).execute()
    }

    suspend fun updateVarietyName(): ApolloResponse<UpdateVarietyNameMutation.Data>? {
        if (uiState.varietyName == navKey.varietyName) {
            return null
        }
        return apolloClient.mutation(
            UpdateVarietyNameMutation(navKey.varietyName, uiState.varietyName)
        ).execute()
    }
}