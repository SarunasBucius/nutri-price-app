package com.github.sarunasbucius.nutriprice.core.navigation.model

import android.net.Uri
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.navigation.NavType
import com.github.sarunasbucius.nutriprice.core.model.PurchaseDetails
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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