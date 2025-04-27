package com.github.sarunasbucius.nutriprice.feature.product.editProductName

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            text = "Edit product and variety name"
        )
        TextField(
            modifier = Modifier.padding(bottom = 8.dp),
            value = viewModel.uiState.productName,
            onValueChange = { viewModel.updateProductName(it) },
            label = { Text("Product name") }
        )
        TextField(
            modifier = Modifier.padding(bottom = 8.dp),
            value = viewModel.uiState.varietyName,
            onValueChange = { viewModel.updateVarietyName(it) },
            label = { Text("Variety name") }
        )
        viewModel.uiState.errors.forEach {
            Text(text = it, color = androidx.compose.ui.graphics.Color.Red)
        }
        Button(onClick = { viewModel.submit() }) {
            Text(text = "Submit")
        }

    }
}