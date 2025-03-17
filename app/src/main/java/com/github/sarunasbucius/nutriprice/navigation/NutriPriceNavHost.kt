package com.github.sarunasbucius.nutriprice.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen

@Composable
fun NutriPriceNavHost(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = NutriPriceScreen.Home,
    ) {
        nutriPriceNavigation()
    }
}
