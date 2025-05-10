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
data class PurchaseDetailsNav(
    val id: String,
    val date: String,
    val retailer: String,
    val price: Double?,
    val amount: Double?,
    val unit: String,
    val notes: String = ""
) : Parcelable


object PurchaseDetailsType : NavType<PurchaseDetailsNav>(isNullableAllowed = false) {
    override fun get(
        bundle: Bundle,
        key: String
    ): PurchaseDetailsNav? = BundleCompat.getParcelable(bundle, key, PurchaseDetailsNav::class.java)

    override fun parseValue(value: String): PurchaseDetailsNav {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: PurchaseDetailsNav
    ) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: PurchaseDetailsNav): String =
        Uri.encode(Json.encodeToString(value))
}