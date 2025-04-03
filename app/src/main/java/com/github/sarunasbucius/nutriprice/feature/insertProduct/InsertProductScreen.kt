package com.github.sarunasbucius.nutriprice.feature.insertProduct

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sarunasbucius.nutriprice.core.design.component.UnitDropdown
import com.github.sarunasbucius.nutriprice.core.model.NutritionalValueUnit
import com.github.sarunasbucius.nutriprice.core.model.QuantityUnit
import com.github.sarunasbucius.nutriprice.core.navigation.currentComposeNavigator
import com.github.sarunasbucius.nutriprice.feature.common.model.NutritionalValueUi

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
        ProductSection(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            uiState = uiState,
            insertProductViewModel = insertProductViewModel
        )

        NutritionalValueSection(
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

@Composable
fun ProductSection(
    modifier: Modifier = Modifier,
    uiState: InsertProductUiState,
    insertProductViewModel: InsertProductViewModel
) {
    Text(
        modifier = modifier
            .padding(8.dp),
        text = "Purchased product details",
    )
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        value = uiState.productName,
        onValueChange = { insertProductViewModel.updateName(it) },
        label = { Text("Product name") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)

    )

    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            value = uiState.price,
            onValueChange = { insertProductViewModel.updatePrice(it) },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            modifier = Modifier.weight(1f),
            value = uiState.amount,
            onValueChange = { insertProductViewModel.updateAmount(it) },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        UnitDropdown(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            unit = uiState.unit,
            onValueChange = {
                insertProductViewModel.updateUnit(
                    it as? QuantityUnit ?: QuantityUnit.UNSPECIFIED
                )
            }
        )
    }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = uiState.notes,
        onValueChange = { insertProductViewModel.updateNotes(it) },
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