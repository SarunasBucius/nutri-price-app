package com.github.sarunasbucius.nutriprice.feature.product

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.github.sarunasbucius.nutriprice.core.navigation.AppComposeNavigator
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarController
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarEvent
import com.github.sarunasbucius.nutriprice.graphql.ProductAggregateQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

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

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val apolloClient: ApolloClient,
    private val navigation: AppComposeNavigator<NutriPriceScreen>,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val productId: String = savedStateHandle["productId"] ?: ""

    var uiState by mutableStateOf(ProductAggregateUi(productId = productId))
        private set

    init {
        fetchProduct()
    }

    fun fetchProduct() {
        viewModelScope.launch(ioDispatcher) {
            uiState = uiState.copy(
                isLoading = true
            )
            val response = apolloClient.query(ProductAggregateQuery(productId)).execute()
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