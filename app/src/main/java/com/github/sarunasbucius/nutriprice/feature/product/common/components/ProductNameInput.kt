package com.github.sarunasbucius.nutriprice.feature.product.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp

@Composable
fun ProductNameInput(
    modifier: Modifier = Modifier,
    productName: String,
    varietyName: String,
    updateProductName: (String) -> Unit,
    updateVarietyName: (String) -> Unit
) {
    Text(
        modifier = modifier.padding(bottom = 8.dp),
        text = "Product details"
    )

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        value = productName,
        onValueChange = { updateProductName(it) },
        label = { Text("Product name") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
    )

    TextField(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        value = varietyName,
        onValueChange = { updateVarietyName(it) },
        label = { Text("Variety name") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
    )
}