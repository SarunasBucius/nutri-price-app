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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sarunasbucius.nutriprice.core.design.component.UnitDropdown
import com.github.sarunasbucius.nutriprice.core.design.theme.CustomRed
import com.github.sarunasbucius.nutriprice.core.model.NutritionalValueUnit
import com.github.sarunasbucius.nutriprice.core.model.QuantityUnit
import com.github.sarunasbucius.nutriprice.core.navigation.currentComposeNavigator
import com.github.sarunasbucius.nutriprice.feature.common.model.NutritionalValueUi
import com.github.sarunasbucius.nutriprice.feature.common.model.PurchasedProduct

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
        EditProductName(
            productName = uiState.productName,
            updateProductName = editProductViewModel::updateProductName
        )

        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            text = "Purchases"
        )
        uiState.purchasedProducts.forEachIndexed { index, purchasedProduct ->
            EditPurchase(
                purchase = purchasedProduct,
                index = index,
                updatePurchasedProduct = editProductViewModel::updatePurchasedProduct,
            )
        }

        NutritionalValueSection(
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

@Composable
fun EditProductName(productName: String, updateProductName: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        value = productName,
        onValueChange = { updateProductName(it) },
        label = { Text("Product name") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
    )
}

@Composable
fun EditPurchase(
    purchase: PurchasedProduct,
    index: Int,
    updatePurchasedProduct: (index: Int, update: (PurchasedProduct) -> PurchasedProduct) -> Unit
) {
    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            value = purchase.price,
            onValueChange = { updatePurchasedProduct(index) { product -> product.copy(price = it) } },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            modifier = Modifier.weight(1f),
            value = purchase.amount,
            onValueChange = { updatePurchasedProduct(index) { product -> product.copy(amount = it) } },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        UnitDropdown(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            unit = purchase.unit,
            onValueChange = {
                updatePurchasedProduct(index) { product ->
                    product.copy(
                        unit = it as? QuantityUnit ?: QuantityUnit.UNSPECIFIED
                    )
                }
            }
        )
    }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        value = purchase.notes,
        onValueChange = { updatePurchasedProduct(index) { product -> product.copy(notes = it) } },
        label = { Text("Notes") },
    )
}

@Composable
fun NutritionalValueSection(
    modifier: Modifier = Modifier,
    nutritionalValues: NutritionalValueUi,
    updateNutritionalValue: (NutritionalValueUi) -> Unit
) {
    Text(
        modifier = modifier
            .padding(8.dp),
        text = "Nutritional values"
    )

    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        UnitDropdown(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
            unit = nutritionalValues.unit,
            units = NutritionalValueUnit.entries,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValues.copy(
                        unit = it as? NutritionalValueUnit ?: NutritionalValueUnit.UNSPECIFIED
                    )
                )
            }
        )

        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            value = nutritionalValues.energyValueKcal,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValues.copy(energyValueKcal = it)
                )
            },
            label = { Text("Energy value (kcal)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }

    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
            value = nutritionalValues.fat,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValues.copy(fat = it)
                )
            },
            label = { Text("Fat") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            value = nutritionalValues.saturatedFat,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValues.copy(saturatedFat = it)
                )
            },
            label = { Text("Saturated fat") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }

    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
            value = nutritionalValues.carbohydrate,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValues.copy(carbohydrate = it)
                )
            },
            label = { Text("Carbohydrate") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            value = nutritionalValues.carbohydrateSugars,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValues.copy(carbohydrateSugars = it)
                )
            },
            label = { Text("Sugars") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }

    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
            value = nutritionalValues.fibre,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValues.copy(fibre = it)
                )
            },
            label = { Text("Fibre") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            value = nutritionalValues.protein,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValues.copy(protein = it)
                )
            },
            label = { Text("Protein") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            value = nutritionalValues.salt,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValues.copy(salt = it)
                )
            },
            label = { Text("Salt") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}