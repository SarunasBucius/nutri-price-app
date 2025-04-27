package com.github.sarunasbucius.nutriprice.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class NewProduct(
    val name: String,
    val purchaseDetails: PurchaseDetails,
    val nutritionalValues: NutritionalValue
)

@Serializable
data class Product(
    val name: String,
    val purchases: List<PurchaseDetails>,
    val nutritionalValues: NutritionalValue
)

@Serializable
@Parcelize
data class PurchaseDetails(
    val id: String,
    val date: String,
    val retailer: String,
    val price: Double?,
    val amount: Double?,
    val unit: String,
    val notes: String = ""
) : Parcelable

@Serializable
@Parcelize
data class NutritionalValue(
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