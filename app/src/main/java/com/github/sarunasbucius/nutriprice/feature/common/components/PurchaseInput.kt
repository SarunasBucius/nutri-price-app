package com.github.sarunasbucius.nutriprice.feature.common.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.sarunasbucius.nutriprice.core.design.component.DatePicker
import com.github.sarunasbucius.nutriprice.core.design.component.UnitDropdown
import com.github.sarunasbucius.nutriprice.feature.common.model.PurchaseDetailsUi

@Composable
fun PurchaseInput(
    modifier: Modifier = Modifier,
    purchase: PurchaseDetailsUi, updatePurchasedProduct: (PurchaseDetailsUi) -> Unit
) {
    Text(
        modifier = modifier
            .padding(8.dp),
        text = "Purchase details",
    )
    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
            value = purchase.retailer,
            onValueChange = {
                updatePurchasedProduct(purchase.copy(retailer = it))
            },
            label = { Text("Retailer") },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        DatePicker(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            label = "Purchase date",
            date = purchase.date,
            onDateChange = {
                updatePurchasedProduct(purchase.copy(date = it))
            }
        )
    }
    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            value = purchase.price,
            onValueChange = {
                updatePurchasedProduct(purchase.copy(price = it))
            },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            modifier = Modifier.weight(1f),
            value = purchase.amount,
            onValueChange = {
                updatePurchasedProduct(purchase.copy(amount = it))
            },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        UnitDropdown(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            unit = purchase.unit,
            onValueChange = {
                updatePurchasedProduct(
                    purchase.copy(unit = it)
                )
            })
    }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        value = purchase.notes,
        onValueChange = {
            updatePurchasedProduct(purchase.copy(notes = it))
        },
        label = { Text("Notes") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
    )
}