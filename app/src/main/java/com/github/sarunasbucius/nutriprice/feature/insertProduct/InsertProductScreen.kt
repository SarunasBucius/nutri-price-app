package com.github.sarunasbucius.nutriprice.feature.insertProduct

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
import com.github.sarunasbucius.nutriprice.core.navigation.currentComposeNavigator
import com.github.sarunasbucius.nutriprice.feature.common.components.NutritionalValueInput
import com.github.sarunasbucius.nutriprice.feature.common.components.ProductNameInput
import com.github.sarunasbucius.nutriprice.feature.common.components.PurchaseInput

@Composable
fun InsertProductScreen(
    insertProductViewModel: InsertProductViewModel = hiltViewModel()
) {
    val uiState = insertProductViewModel.uiState
    val composeNavigator = currentComposeNavigator

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        ProductNameInput(
            productName = uiState.productName,
            updateProductName = insertProductViewModel::updateName
        )

        PurchaseInput(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            purchase = uiState.purchaseDetails,
            updatePurchasedProduct = insertProductViewModel::updatePurchaseDetails
        )

        NutritionalValueInput(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            nutritionalValue = uiState.nutritionalValues,
            updateNutritionalValue = insertProductViewModel::updateNutritionalValue
        )

        uiState.errors.forEach {
            Text(text = it, color = androidx.compose.ui.graphics.Color.Red)
        }

        Button(onClick = { insertProductViewModel.insertProduct { composeNavigator.navigateUp() } }) {
            Text(text = "Insert product")
        }
    }
}