package com.github.sarunasbucius.nutriprice.core.navigation

import androidx.compose.runtime.mutableStateListOf
import com.github.sarunasbucius.nutriprice.graphql.ProductAggregateQuery
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {
    val backStack = mutableStateListOf<NutriPriceScreen>(NutriPriceScreen.Home)
    private val _navigationResults = MutableSharedFlow<NavigationResult>(extraBufferCapacity = 1)
    val navigationResults = _navigationResults.asSharedFlow()

    fun navigateUp() {
        backStack.removeLastOrNull()
    }

    fun sendResult(result: NavigationResult) {
        _navigationResults.tryEmit(result)
    }

    fun sendResultAndNavigateUp(result: NavigationResult) {
        sendResult(result)
        navigateUp()
    }
}

sealed class NavigationResult {
    data class ProductName(
        val productId: String,
        val productName: String,
        val variety: String,
        var oldVariety: String,
    ) : NavigationResult()

    data class NutritionalValue(
        val productId: String,
        val varietyName: String,
        val nutritionalValue: ProductAggregateQuery.NutritionalValue,
    ) : NavigationResult()
}
