package com.github.sarunasbucius.nutriprice.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.feature.home.HomeScreen
import com.github.sarunasbucius.nutriprice.feature.insertProduct.InsertProductScreen
import com.github.sarunasbucius.nutriprice.feature.productList.ProductListScreen

fun NavGraphBuilder.nutriPriceNavigation() {
    composable<NutriPriceScreen.Home> {
        HomeScreen()
    }

    composable<NutriPriceScreen.ProductList> {
        ProductListScreen()
    }

    composable<NutriPriceScreen.InsertProduct> {
        InsertProductScreen()
    }
}
