package com.github.sarunasbucius.nutriprice.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.feature.home.HomeScreen
import com.github.sarunasbucius.nutriprice.feature.product.editNutritionalValue.EditNutritionalValueScreen
import com.github.sarunasbucius.nutriprice.feature.product.editProductName.EditProductNameScreen
import com.github.sarunasbucius.nutriprice.feature.product.editPurchase.EditPurchaseScreen
import com.github.sarunasbucius.nutriprice.feature.product.insertProduct.InsertProductScreen
import com.github.sarunasbucius.nutriprice.feature.product.productList.ProductListScreen
import com.github.sarunasbucius.nutriprice.feature.product.viewProduct.ProductScreen
import com.github.sarunasbucius.nutriprice.feature.recipe.planRecipes.PlanRecipesScreen
import com.github.sarunasbucius.nutriprice.feature.recipe.preparedRecipes.PreparedRecipesScreen
import com.github.sarunasbucius.nutriprice.feature.recipe.recipeList.RecipeListScreen
import com.github.sarunasbucius.nutriprice.feature.recipe.upsertRecipe.UpsertRecipeScreen
import com.github.sarunasbucius.nutriprice.feature.recipe.viewPreparedRecipe.PreparedRecipeScreen
import com.github.sarunasbucius.nutriprice.feature.recipe.viewRecipe.RecipeScreen

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

    composable<NutriPriceScreen.UpsertRecipe> {
        UpsertRecipeScreen()
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

    composable<NutriPriceScreen.EditNutritionalValue>(
        typeMap = NutriPriceScreen.EditNutritionalValue.typeMap
    ) {
        EditNutritionalValueScreen()
    }

    composable<NutriPriceScreen.Recipe> {
        RecipeScreen()
    }

    composable<NutriPriceScreen.PreparedRecipes> {
        PreparedRecipesScreen()
    }

    composable<NutriPriceScreen.PlanRecipes> {
        PlanRecipesScreen()
    }

    composable<NutriPriceScreen.PreparedRecipe> {
        PreparedRecipeScreen()
    }
}
