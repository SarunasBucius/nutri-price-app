package com.github.sarunasbucius.nutriprice.feature.product.productList

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.github.sarunasbucius.nutriprice.core.navigation.NavigationManager
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarController
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarEvent
import com.github.sarunasbucius.nutriprice.graphql.ProductsQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val apolloClient: ApolloClient,
    private val navigation: NavigationManager,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Idle)
    internal val uiState = _uiState.asStateFlow()

    private val _productList = MutableStateFlow<List<ProductsQuery.Product>>(emptyList())
    val productList = _productList.asStateFlow()

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.emit(ProductListUiState.Loading)
            val response = apolloClient.query(ProductsQuery()).execute()
            if (!response.errors.isNullOrEmpty()) {
                SnackbarController.sendEvent(SnackbarEvent("ERROR: something went wrong"))
                navigation.navigateUp()
            } else {
                _productList.emit(response.data?.products ?: emptyList())
                _uiState.emit(ProductListUiState.Idle)
            }
        }
    }
}

@Stable
internal sealed interface ProductListUiState {
    data object Idle : ProductListUiState
    data object Loading : ProductListUiState
    data class Error(val message: String) : ProductListUiState
}