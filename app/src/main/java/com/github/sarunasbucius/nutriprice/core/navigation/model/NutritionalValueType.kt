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
data class NutritionalValueNav(
    val unit: String? = "",
    val energyValueKcal: Double? = 0.0,
    val fat: Double? = 0.0,
    val saturatedFat: Double? = 0.0,
    val carbohydrate: Double? = 0.0,
    val carbohydrateSugars: Double? = 0.0,
    val fibre: Double? = 0.0,
    val protein: Double? = 0.0,
    val salt: Double? = 0.0,
) : Parcelable

object NutritionalValueType : NavType<NutritionalValueNav>(isNullableAllowed = false) {
    override fun get(
        bundle: Bundle,
        key: String
    ): NutritionalValueNav? =
        BundleCompat.getParcelable(bundle, key, NutritionalValueNav::class.java)

    override fun parseValue(value: String): NutritionalValueNav {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: NutritionalValueNav
    ) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: NutritionalValueNav): String =
        Uri.encode(Json.encodeToString(value))
}