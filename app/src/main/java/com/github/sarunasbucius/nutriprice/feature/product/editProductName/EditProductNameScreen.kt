package com.github.sarunasbucius.nutriprice.feature.product.editProductName

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun EditProductNameScreen(viewModel: EditProductNameViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Edit product and variety name")
        TextField(
            value = viewModel.uiState.productName,
            onValueChange = { viewModel.updateProductName(it) },
            label = { Text("Product name") }
        )
        TextField(
            value = viewModel.uiState.varietyName,
            onValueChange = { viewModel.updateVarietyName(it) },
            label = { Text("Variety name") }
        )
        Button(onClick = { viewModel.submit() }) {
            Text(text = "Submit")
        }

    }
}