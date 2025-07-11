package com.github.sarunasbucius.nutriprice.feature.product.insertProduct

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sarunasbucius.nutriprice.core.navigation.LocalBackStack
import com.github.sarunasbucius.nutriprice.feature.product.common.components.NutritionalValueInput
import com.github.sarunasbucius.nutriprice.feature.product.common.components.ProductNameInput
import com.github.sarunasbucius.nutriprice.feature.product.common.components.PurchaseInput

@Composable
fun InsertProductScreen(
    insertProductViewModel: InsertProductViewModel = hiltViewModel()
) {
    val uiState = insertProductViewModel.uiState
    val composeNavigator = LocalBackStack.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding())
    ) {
        ProductNameInput(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            productName = uiState.productName,
            varietyName = uiState.varietyName,
            updateProductName = insertProductViewModel::updateName,
            updateVarietyName = insertProductViewModel::updateVarietyName
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
            Text(text = it, color = Color.Red)
        }

        Button(onClick = { insertProductViewModel.insertProduct { composeNavigator.removeLastOrNull() } }) {
            Text(text = "Insert product")
        }
    }
}