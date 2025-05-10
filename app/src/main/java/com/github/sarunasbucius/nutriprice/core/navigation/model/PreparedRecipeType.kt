package com.github.sarunasbucius.nutriprice.core.navigation.model

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
@Parcelize
data class PreparedRecipeNav(
    val name: String,
    val ingredients: List<IngredientNav>,
    val steps: List<String> = emptyList(),
    val notes: String,
    val preparedDate: String,
    val portion: Double,
) : Parcelable

@Serializable
@Parcelize
data class IngredientNav(
    val product: String,
    val amount: Double?,
    val unit: String,
    val notes: String
) : Parcelable

object PreparedRecipeType : NavType<PreparedRecipeNav>(isNullableAllowed = false) {
    override fun get(
        bundle: Bundle,
        key: String
    ): PreparedRecipeNav? = BundleCompat.getParcelable(bundle, key, PreparedRecipeNav::class.java)

    override fun parseValue(value: String): PreparedRecipeNav {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: PreparedRecipeNav
    ) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: PreparedRecipeNav): String =
        Uri.encode(Json.encodeToString(value))
}