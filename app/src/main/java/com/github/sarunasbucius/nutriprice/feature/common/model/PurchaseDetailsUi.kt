package com.github.sarunasbucius.nutriprice.feature.common.model

import com.github.sarunasbucius.nutriprice.core.model.PurchaseDetails

data class PurchaseDetailsUi(
    val id: String = "",
    val date: String = "",
    val retailer: String = "",
    val price: String = "",
    val notes: String = "",
    val amount: String = "",
    val unit: String = "",
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
            id = id,
            price = price.toDoubleOrNull(),
            amount = amount.toDoubleOrNull(),
            unit = unit,
            notes = notes,
            date = date,
            retailer = retailer,
        )
    }

    companion object {
        fun fromApiModel(purchaseDetails: PurchaseDetails): PurchaseDetailsUi {
            return PurchaseDetailsUi(
                id = purchaseDetails.id,
                date = purchaseDetails.date,
                retailer = purchaseDetails.retailer,
                price = purchaseDetails.price?.toString() ?: "",
                notes = purchaseDetails.notes,
                amount = purchaseDetails.amount?.toString() ?: "",
                unit = purchaseDetails.unit,
            )
        }
    }
}