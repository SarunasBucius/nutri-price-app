package com.github.sarunasbucius.nutriprice.core.navigation

import android.net.Uri
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.navigation.NavType
import com.github.sarunasbucius.nutriprice.core.model.PurchaseDetails
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
}

object PurchaseDetailsType : NavType<PurchaseDetails>(isNullableAllowed = false) {
    override fun get(
        bundle: Bundle,
        key: String
    ): PurchaseDetails? = BundleCompat.getParcelable(bundle, key, PurchaseDetails::class.java)

    override fun parseValue(value: String): PurchaseDetails {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: PurchaseDetails
    ) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: PurchaseDetails): String =
        Uri.encode(Json.encodeToString(value))
}