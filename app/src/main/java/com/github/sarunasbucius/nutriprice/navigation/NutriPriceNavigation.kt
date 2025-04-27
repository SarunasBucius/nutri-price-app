package com.github.sarunasbucius.nutriprice.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.feature.home.HomeScreen
import com.github.sarunasbucius.nutriprice.feature.insertProduct.InsertProductScreen
import com.github.sarunasbucius.nutriprice.feature.insertRecipe.InsertRecipeScreen
import com.github.sarunasbucius.nutriprice.feature.product.ProductScreen
import com.github.sarunasbucius.nutriprice.feature.product.editProductName.EditProductNameScreen
import com.github.sarunasbucius.nutriprice.feature.product.editPurchase.EditPurchaseScreen
import com.github.sarunasbucius.nutriprice.feature.productList.ProductListScreen
import com.github.sarunasbucius.nutriprice.feature.recipeList.RecipeListScreen

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

    composable<NutriPriceScreen.RecipeList> {
        RecipeListScreen()
    }

    composable<NutriPriceScreen.InsertRecipe> {
        InsertRecipeScreen()
    }

    composable<NutriPriceScreen.Product> {
        ProductScreen()
    }

    composable<NutriPriceScreen.EditProductName> {
        EditProductNameScreen()
    }

    composable<NutriPriceScreen.EditPurchase>(
        typeMap = NutriPriceScreen.EditPurchase.typeMap
    ) {
        EditPurchaseScreen()
    }
}
