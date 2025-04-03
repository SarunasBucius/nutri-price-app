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

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            text = "Purchase details",
        )

        PurchaseInput(
            purchase = uiState.purchaseDetails,
            updatePurchasedProduct = insertProductViewModel::updatePurchaseDetails
        )

        NutritionalValueInput(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            nutritionalValues = uiState.nutritionalValues,
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
//
//@Composable
//fun ProductSection(
//    modifier: Modifier = Modifier,
//    uiState: InsertProductUiState,
//    insertProductViewModel: InsertProductViewModel
//) {
//    Text(
//        modifier = modifier
//            .padding(8.dp),
//        text = "Purchased product details",
//    )
//    TextField(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(bottom = 8.dp),
//        value = uiState.productName,
//        onValueChange = { insertProductViewModel.updateName(it) },
//        label = { Text("Product name") },
//        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
//
//    )
//
//    Row(modifier = Modifier.padding(bottom = 8.dp)) {
//        TextField(
//            modifier = Modifier
//                .weight(1f)
//                .padding(end = 8.dp),
//            value = uiState.price,
//            onValueChange = { insertProductViewModel.updatePrice(it) },
//            label = { Text("Price") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//        )
//        TextField(
//            modifier = Modifier.weight(1f),
//            value = uiState.amount,
//            onValueChange = { insertProductViewModel.updateAmount(it) },
//            label = { Text("Amount") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//        )
//
//        UnitDropdown(
//            modifier = Modifier
//                .weight(1f)
//                .padding(start = 8.dp),
//            unit = uiState.unit,
//            onValueChange = {
//                insertProductViewModel.updateUnit(
//                    it as? QuantityUnit ?: QuantityUnit.UNSPECIFIED
//                )
//            }
//        )
//    }
//
//    TextField(
//        modifier = Modifier.fillMaxWidth(),
//        value = uiState.notes,
//        onValueChange = { insertProductViewModel.updateNotes(it) },
//        label = { Text("Notes") },
//    )
//}