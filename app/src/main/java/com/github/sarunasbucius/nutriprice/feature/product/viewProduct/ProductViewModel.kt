package com.github.sarunasbucius.nutriprice.feature.product.viewProduct

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.github.sarunasbucius.nutriprice.core.navigation.NavigationManager
import com.github.sarunasbucius.nutriprice.core.navigation.NavigationResult
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarController
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarEvent
import com.github.sarunasbucius.nutriprice.graphql.ProductAggregateQuery
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

data class ProductAggregateUi(
    val productAggregate: ProductAggregateQuery.ProductAggregate = ProductAggregateQuery.ProductAggregate(
        "",
        emptyList()
    ),
    val selectedVariety: ProductAggregateQuery.Variety = ProductAggregateQuery.Variety(
        "",
        null,
        emptyList()
    ),
    val isLoading: Boolean = true,
    val productId: String
)

@HiltViewModel(assistedFactory = ProductViewModel.Factory::class)
class ProductViewModel @AssistedInject constructor(
    @Assisted val navKey: NutriPriceScreen.Product,
    private val apolloClient: ApolloClient,
    internal val navigation: NavigationManager,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    var uiState by mutableStateOf(ProductAggregateUi(productId = navKey.productId))
        private set

    init {
        viewModelScope.launch {
            navigation.navigationResults.collect { result ->
                when (result) {
                    is NavigationResult.ProductName -> {
                        processProductNameUpdate(result)
                    }

                    is NavigationResult.NutritionalValue -> {
                        processNutritionalValueUpdate(result)
                    }

                    else -> {}
                }
            }
        }
        fetchProduct()
    }

    @AssistedFactory
    interface Factory {
        fun create(navKey: NutriPriceScreen.Product): ProductViewModel
    }

    fun processNutritionalValueUpdate(result: NavigationResult.NutritionalValue) {
        val updatedVarieties = uiState.productAggregate.varieties.map { currentVariety ->
            if (currentVariety.varietyName == result.varietyName) {
                currentVariety.copy(
                    nutritionalValue = result.nutritionalValue
                )
            } else {
                currentVariety
            }
        }
        uiState = uiState.copy(
            productAggregate = uiState.productAggregate.copy(
                varieties = updatedVarieties
            )
        )
        selectVariety(result.varietyName)
    }

    fun processProductNameUpdate(result: NavigationResult.ProductName) {
        val updatedVarieties = uiState.productAggregate.varieties.map { currentVariety ->
            if (currentVariety.varietyName == result.oldVariety) {
                currentVariety.copy(
                    varietyName = result.variety
                )
            } else {
                currentVariety
            }
        }
        uiState = uiState.copy(
            productAggregate = uiState.productAggregate.copy(
                name = result.productName,
                varieties = updatedVarieties
            ),
            selectedVariety = uiState.selectedVariety.copy(
                varietyName = result.variety
            )
        )
    }

    fun fetchProduct() {
        viewModelScope.launch(ioDispatcher) {
            uiState = uiState.copy(
                isLoading = true
            )
            val response = apolloClient.query(ProductAggregateQuery(navKey.productId)).execute()
            val data = response.data
            if (response.hasErrors() || data == null || data.productAggregate.varieties.isEmpty()) {
                if (response.hasErrors()) {
                    Log.e("ProductViewModel", response.errors.toString())
                }
                SnackbarController.sendEvent(SnackbarEvent("ERROR: something went wrong"))
                navigation.navigateUp()
                return@launch
            }

            val varietyIndex =
                data.productAggregate.varieties.indexOfFirst { it.varietyName == data.productAggregate.name }
                    .takeIf { it != -1 } ?: 0
            uiState = uiState.copy(
                productAggregate = data.productAggregate,
                selectedVariety = data.productAggregate.varieties[varietyIndex],
                isLoading = false
            )
        }
    }

    fun selectVariety(varietyName: String) {
        if (uiState.productAggregate.varieties.isEmpty()) {
            return
        }

        val variety = uiState.productAggregate.varieties.find { it.varietyName == varietyName }
            ?: uiState.productAggregate.varieties[0]
        uiState = uiState.copy(selectedVariety = variety)
    }
}