package com.github.sarunasbucius.nutriprice.feature.product.common.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.sarunasbucius.nutriprice.core.design.component.UnitDropdown
import com.github.sarunasbucius.nutriprice.feature.product.common.model.NutritionalValueUi


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NutritionalValueInput(
    modifier: Modifier = Modifier,
    nutritionalValue: NutritionalValueUi,
    updateNutritionalValue: (NutritionalValueUi) -> Unit
) {
    val focusManager = LocalFocusManager.current
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
            unit = nutritionalValue.unit,
            units = listOf("", "100 g", "100 ml", "1 piece"),
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValue.copy(unit = it)
                )
            }
        )

        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            value = nutritionalValue.energyValueKcal,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValue.copy(energyValueKcal = it)
                )
            },
            label = { Text("Energy value (kcal)", maxLines = 1) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Next) }
            ),
        )
    }

    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
            value = nutritionalValue.fat,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValue.copy(fat = it)
                )
            },
            label = { Text("Fat") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Next) }
            ),
        )

        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            value = nutritionalValue.saturatedFat,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValue.copy(saturatedFat = it)
                )
            },
            label = { Text("Saturated fat") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Next) }
            ),
        )
    }

    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
            value = nutritionalValue.carbohydrate,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValue.copy(carbohydrate = it)
                )
            },
            label = { Text("Carbohydrate") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Next) }
            ),
        )

        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            value = nutritionalValue.carbohydrateSugars,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValue.copy(carbohydrateSugars = it)
                )
            },
            label = { Text("Sugars") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Next) }
            ),
        )
    }

    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
            value = nutritionalValue.fibre,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValue.copy(fibre = it)
                )
            },
            label = { Text("Fibre") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Next) }
            ),
        )

        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            value = nutritionalValue.protein,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValue.copy(protein = it)
                )
            },
            label = { Text("Protein") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Next) }
            ),
        )

        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            value = nutritionalValue.salt,
            onValueChange = {
                updateNutritionalValue(
                    nutritionalValue.copy(salt = it)
                )
            },
            label = { Text("Salt") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Exit) }
            ),
        )
    }
}