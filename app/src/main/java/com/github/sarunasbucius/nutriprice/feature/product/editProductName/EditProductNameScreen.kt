package com.github.sarunasbucius.nutriprice.feature.product.editProductName

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sarunasbucius.nutriprice.feature.product.common.components.ProductNameInput

@Composable
fun EditProductNameScreen(viewModel: EditProductNameViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ProductNameInput(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            productName = viewModel.uiState.productName,
            varietyName = viewModel.uiState.varietyName,
            updateProductName = { viewModel.updateProductName(it) },
            updateVarietyName = { viewModel.updateVarietyName(it) }
        )
        viewModel.uiState.errors.forEach {
            Text(text = it, color = Color.Red)
        }
        Button(onClick = { viewModel.submit() }) {
            Text(text = "Submit")
        }

    }
}