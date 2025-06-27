package com.github.sarunasbucius.nutriprice.feature.product.productList

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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductListUiState(
    val isLoading: Boolean = true,
    val searchInput: String = "",
)

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val apolloClient: ApolloClient,
    private val navigation: NavigationManager,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState())
    internal val uiState = _uiState.asStateFlow()

    private val _productList = MutableStateFlow<List<ProductsQuery.Product>>(emptyList())

    val filteredProductList: StateFlow<List<ProductsQuery.Product>> =
        uiState
            .map { state ->
                val input = state.searchInput.trim()
                if (input.isBlank()) {
                    _productList.value
                } else {
                    _productList.value.filter {
                        it.name.contains(input, ignoreCase = true)
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    init {
        fetchProducts()
    }


    private fun fetchProducts() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            val response = apolloClient.query(ProductsQuery()).execute()
            if (!response.errors.isNullOrEmpty()) {
                SnackbarController.sendEvent(SnackbarEvent("ERROR: something went wrong"))
                navigation.navigateUp()
            } else {
                _productList.update { response.data?.products ?: emptyList() }
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onSearchInputChanged(newInput: String) {
        _uiState.update { it.copy(searchInput = newInput) }
    }
}