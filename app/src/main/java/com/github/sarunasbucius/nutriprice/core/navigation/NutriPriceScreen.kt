package com.github.sarunasbucius.nutriprice.core.navigation

import kotlinx.serialization.Serializable


sealed interface NutriPriceScreen {
    @Serializable
    data object Home : NutriPriceScreen

    @Serializable
    data object ProductList : NutriPriceScreen

    @Serializable
    data object InsertProduct : NutriPriceScreen

    @Serializable
    data object RecipeList : NutriPriceScreen
}
