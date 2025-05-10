package com.github.sarunasbucius.nutriprice.feature.product.common.model

import com.github.sarunasbucius.nutriprice.core.navigation.model.PurchaseDetailsNav
import com.github.sarunasbucius.nutriprice.graphql.type.PurchaseInput

data class PurchaseDetailsUi(
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

    fun toApiModel(): PurchaseInput {
        return PurchaseInput(
            price = price.toDoubleOrNull() ?: 0.0,
            quantity = amount.toDoubleOrNull() ?: 0.0,
            unit = unit,
            notes = notes,
            date = date,
            retailer = retailer,
        )
    }

    fun isEmpty(): Boolean {
        return date.isEmpty() && retailer.isEmpty() && price.isEmpty() && notes.isEmpty() && amount.isEmpty() && unit.isEmpty()
    }

    companion object {
        fun fromApiModel(purchaseDetails: PurchaseDetailsNav): PurchaseDetailsUi {
            return PurchaseDetailsUi(
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