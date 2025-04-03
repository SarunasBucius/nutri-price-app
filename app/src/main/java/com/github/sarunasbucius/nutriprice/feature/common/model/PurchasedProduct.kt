package com.github.sarunasbucius.nutriprice.feature.common.model

import com.github.sarunasbucius.nutriprice.core.model.PurchaseDetails
import com.github.sarunasbucius.nutriprice.core.model.QuantityUnit

data class PurchasedProduct(
    val price: String = "",
    val notes: String = "",
    val amount: String = "",
    val unit: QuantityUnit = QuantityUnit.UNSPECIFIED,
) {
    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (price.isNotEmpty() && price.toDoubleOrNull() == null) {
            errors.add("Price must be a number")
        }
        if (amount.isNotEmpty() && amount.toDoubleOrNull() == null) {
            errors.add("Amount must be a number")
        }
        return errors
    }

    fun toApiModel(): PurchaseDetails {
        return PurchaseDetails(
            price = price.toDoubleOrNull(),
            amount = amount.toDoubleOrNull(),
            unit = unit,
            notes = notes,
        )
    }

    companion object {
        fun fromApiModel(purchaseDetails: PurchaseDetails): PurchasedProduct {
            return PurchasedProduct(
                price = purchaseDetails.price?.toString() ?: "",
                notes = purchaseDetails.notes,
                amount = purchaseDetails.amount?.toString() ?: "",
                unit = purchaseDetails.unit ?: QuantityUnit.UNSPECIFIED,
            )
        }
    }
}