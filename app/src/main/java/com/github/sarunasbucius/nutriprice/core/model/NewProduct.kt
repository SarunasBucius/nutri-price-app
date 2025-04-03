package com.github.sarunasbucius.nutriprice.core.model

import kotlinx.serialization.Serializable

@Serializable
data class NewProduct(
    val name: String,
    val price: Double?,
    val amount: Double?,
    val unit: QuantityUnit?,
    val notes: String,
    val nutritionalValues: NutritionalValue
)

@Serializable
data class Product(
    val name: String,
    val purchases: List<PurchaseDetails>,
    val nutritionalValues: NutritionalValue
)

@Serializable
data class PurchaseDetails(
    val price: Double?,
    val amount: Double?,
    val unit: QuantityUnit?,
    val notes: String = ""
)

@Serializable
data class NutritionalValue(
    val unit: NutritionalValueUnit?,
    val energyValueKcal: Double?,
    val fat: Double?,
    val saturatedFat: Double?,
    val carbohydrate: Double?,
    val carbohydrateSugars: Double?,
    val fibre: Double?,
    val protein: Double?,
    val salt: Double?,
)