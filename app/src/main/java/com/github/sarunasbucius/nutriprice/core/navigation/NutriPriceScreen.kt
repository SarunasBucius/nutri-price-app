package com.github.sarunasbucius.nutriprice.core.navigation

import com.github.sarunasbucius.nutriprice.core.navigation.model.NutritionalValueNav
import com.github.sarunasbucius.nutriprice.core.navigation.model.NutritionalValueType
import com.github.sarunasbucius.nutriprice.core.navigation.model.PreparedRecipeNav
import com.github.sarunasbucius.nutriprice.core.navigation.model.PreparedRecipeType
import com.github.sarunasbucius.nutriprice.core.navigation.model.PurchaseDetailsNav
import com.github.sarunasbucius.nutriprice.core.navigation.model.PurchaseDetailsType
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

sealed interface NutriPriceScreen {
    @Serializable
    data object Home : NutriPriceScreen

    @Serializable
    data object ProductList : NutriPriceScreen

    @Serializable
    data object InsertProduct : NutriPriceScreen

    @Serializable
    data object RecipeList : NutriPriceScreen

    @Serializable
    data class UpsertRecipe(val recipeName: String? = null) : NutriPriceScreen

    @Serializable
    data class Product(val productId: String) : NutriPriceScreen

    @Serializable
    data class EditProductName(
        val productId: String,
        val productName: String,
        val varietyName: String
    ) : NutriPriceScreen

    @Serializable
    data class EditPurchase(val purchaseDetails: PurchaseDetailsNav) : NutriPriceScreen {
        companion object {
            val typeMap = mapOf(typeOf<PurchaseDetailsNav>() to PurchaseDetailsType)
        }
    }

    @Serializable
    data class EditNutritionalValue(
        val productId: String,
        val varietyName: String,
        val nutritionalValue: NutritionalValueNav
    ) : NutriPriceScreen {
        companion object {
            val typeMap = mapOf(typeOf<NutritionalValueNav>() to NutritionalValueType)
        }
    }

    @Serializable
    data class Recipe(val recipeName: String) : NutriPriceScreen

    @Serializable
    data object PreparedRecipes : NutriPriceScreen

    @Serializable
    data class PlanRecipes(val date: String) : NutriPriceScreen

    @Serializable
    data class PreparedRecipe(val recipeName: String, val preparedDate: String) : NutriPriceScreen

    @Serializable
    data class EditPreparedRecipe(val preparedRecipe: PreparedRecipeNav) : NutriPriceScreen {
        companion object {
            val typeMap = mapOf(typeOf<PreparedRecipeNav>() to PreparedRecipeType)
        }
    }
}

