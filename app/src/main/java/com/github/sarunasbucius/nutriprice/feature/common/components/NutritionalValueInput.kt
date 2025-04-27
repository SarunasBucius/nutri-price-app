package com.github.sarunasbucius.nutriprice.feature.common.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.sarunasbucius.nutriprice.core.design.component.UnitDropdown
import com.github.sarunasbucius.nutriprice.feature.common.model.NutritionalValueUi


@Composable
fun NutritionalValueInput(
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
            units = listOf("", "100 g", "100 ml", "1 piece"),
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValues.copy(unit = it)
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