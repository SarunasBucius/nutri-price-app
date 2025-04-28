package com.github.sarunasbucius.nutriprice.core.navigation

import com.github.sarunasbucius.nutriprice.core.model.NutritionalValue
import com.github.sarunasbucius.nutriprice.core.model.PurchaseDetails
import com.github.sarunasbucius.nutriprice.core.navigation.model.NutritionalValueType
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
    data object InsertRecipe : NutriPriceScreen

    @Serializable
    data class Product(val productId: String) : NutriPriceScreen

    @Serializable
    data class EditProductName(
        val productId: String,
        val productName: String,
        val varietyName: String
    ) : NutriPriceScreen

    @Serializable
    data class EditPurchase(val purchaseDetails: PurchaseDetails) : NutriPriceScreen {
        companion object {
            val typeMap = mapOf(typeOf<PurchaseDetails>() to PurchaseDetailsType)
        }
    }

    @Serializable
    data class EditNutritionalValue(
        val productId: String,
        val varietyName: String,
        val nutritionalValue: NutritionalValue
    ) : NutriPriceScreen {
        companion object {
            val typeMap = mapOf(typeOf<NutritionalValue>() to NutritionalValueType)
        }
    }

    @Serializable
    data class Recipe(val recipeId: String) : NutriPriceScreen
}

