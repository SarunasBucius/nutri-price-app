package com.github.sarunasbucius.nutriprice.core.navigation.model

import android.net.Uri
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.navigation.NavType
import com.github.sarunasbucius.nutriprice.core.model.NutritionalValue
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object NutritionalValueType : NavType<NutritionalValue>(isNullableAllowed = false) {
    override fun get(
        bundle: Bundle,
        key: String
    ): NutritionalValue? = BundleCompat.getParcelable(bundle, key, NutritionalValue::class.java)

    override fun parseValue(value: String): NutritionalValue {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: NutritionalValue
    ) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: NutritionalValue): String =
        Uri.encode(Json.encodeToString(value))
}