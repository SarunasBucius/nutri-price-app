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
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
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

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            text = "Nutritional values"
        )

        Row(modifier = Modifier.padding(bottom = 8.dp)) {
            UnitDropdown(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 2.dp),
                unit = uiState.nutritionalValues.unit,
                units = NutritionalValueUnit.entries,
                onValueChange = {
                    insertProductViewModel.updateNutritionalValue(
                        uiState.nutritionalValues.copy(
                            unit = it as? NutritionalValueUnit ?: NutritionalValueUnit.UNSPECIFIED
                        )
                    )
                }
            )

            TextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 2.dp),
                value = uiState.nutritionalValues.energyValueKcal,
                onValueChange = {
                    insertProductViewModel.updateNutritionalValue(
                        uiState.nutritionalValues.copy(energyValueKcal = it)
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
                    .padding(end = 2.dp),
                value = uiState.nutritionalValues.fat,
                onValueChange = {
                    insertProductViewModel.updateNutritionalValue(
                        uiState.nutritionalValues.copy(fat = it)
                    )
                },
                label = { Text("Fat") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            TextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 2.dp),
                value = uiState.nutritionalValues.saturatedFat,
                onValueChange = {
                    insertProductViewModel.updateNutritionalValue(
                        uiState.nutritionalValues.copy(saturatedFat = it)
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
                    .padding(end = 2.dp),
                value = uiState.nutritionalValues.carbohydrate,
                onValueChange = {
                    insertProductViewModel.updateNutritionalValue(
                        uiState.nutritionalValues.copy(carbohydrate = it)
                    )
                },
                label = { Text("Carbohydrate") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            TextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 2.dp),
                value = uiState.nutritionalValues.carbohydrateSugars,
                onValueChange = {
                    insertProductViewModel.updateNutritionalValue(
                        uiState.nutritionalValues.copy(carbohydrateSugars = it)
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
                    .padding(end = 2.dp),
                value = uiState.nutritionalValues.fibre,
                onValueChange = {
                    insertProductViewModel.updateNutritionalValue(
                        uiState.nutritionalValues.copy(fibre = it)
                    )
                },
                label = { Text("Fibre") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            TextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp),
                value = uiState.nutritionalValues.protein,
                onValueChange = {
                    insertProductViewModel.updateNutritionalValue(
                        uiState.nutritionalValues.copy(protein = it)
                    )
                },
                label = { Text("Protein") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            TextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 2.dp),
                value = uiState.nutritionalValues.salt,
                onValueChange = {
                    insertProductViewModel.updateNutritionalValue(
                        uiState.nutritionalValues.copy(salt = it)
                    )
                },
                label = { Text("Salt") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        uiState.errors.forEach {
            Text(text = it, color = androidx.compose.ui.graphics.Color.Red)
        }

        Button(onClick = { insertProductViewModel.insertProduct { composeNavigator.navigateUp() } }) {
            Text(text = "Insert product")
        }
    }
}
