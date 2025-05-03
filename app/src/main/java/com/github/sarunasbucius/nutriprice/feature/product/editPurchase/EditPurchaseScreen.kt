package com.github.sarunasbucius.nutriprice.feature.product.editPurchase

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sarunasbucius.nutriprice.feature.product.common.components.PurchaseInput

@Composable
fun EditPurchaseScreen(viewModel: EditPurchaseViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PurchaseInput(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            purchase = uiState.purchase,
            updatePurchasedProduct = viewModel::updatePurchase
        )
        uiState.errors.forEach {
            Text(text = it, color = androidx.compose.ui.graphics.Color.Red)
        }
        Button(onClick = { viewModel.submit() }) {
            Text(text = "Submit")
        }
    }
}