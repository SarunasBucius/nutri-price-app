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
data class NutritionalValue(
    val unit: String?,
    val energyValueKcal: Double?,
    val fat: Double?,
    val saturatedFat: Double?,
    val carbohydrate: Double?,
    val carbohydrateSugars: Double?,
    val fibre: Double?,
    val protein: Double?,
    val salt: Double?,
)