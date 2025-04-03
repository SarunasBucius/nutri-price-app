package com.github.sarunasbucius.nutriprice.feature.editProduct

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sarunasbucius.nutriprice.core.design.theme.CustomRed
import com.github.sarunasbucius.nutriprice.core.navigation.currentComposeNavigator
import com.github.sarunasbucius.nutriprice.feature.common.components.NutritionalValueInput
import com.github.sarunasbucius.nutriprice.feature.common.components.ProductNameInput
import com.github.sarunasbucius.nutriprice.feature.common.components.PurchaseInput

// TODO some sections are exactly same as InsertProductScreen, refactor
@Composable
fun EditProductScreen(editProductViewModel: EditProductViewModel = hiltViewModel()) {
    val uiState = editProductViewModel.uiState
    val composeNavigator = currentComposeNavigator

    LaunchedEffect(key1 = Unit) {
        editProductViewModel.fetchProduct()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding())
    ) {
        ProductNameInput(
            productName = uiState.productName,
            updateProductName = editProductViewModel::updateProductName
        )

        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            text = "Purchase details"
        )
        uiState.purchasedProducts.forEachIndexed { index, purchasedProduct ->
            PurchaseInput(
                purchase = purchasedProduct,
                updatePurchasedProduct = {
                    editProductViewModel.updatePurchaseDetails(index, it)
                }
            )
        }

        NutritionalValueInput(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            nutritionalValues = uiState.nutritionalValues,
            updateNutritionalValue = editProductViewModel::updateNutritionalValue
        )

        uiState.errors.distinct().forEach {
            Text(text = it, color = CustomRed)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { editProductViewModel.insertProduct { composeNavigator.navigateUp() } }
            ) {
                Text(text = "Insert product")
            }
            Button(
                onClick = { editProductViewModel.deleteProduct { composeNavigator.navigateUp() } },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomRed
                )
            ) {
                Text(text = "Delete product")
            }
        }
    }
}