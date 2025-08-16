package com.github.sarunasbucius.nutriprice.core.design.component

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitDropdown(
    modifier: Modifier = Modifier,
    unit: String,
    unitDisplay: Map<String, String>? = null,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val baseUnits = listOf("", "g", "ml", "pcs")

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        TextField(
            value = unitDisplay?.get(unit) ?: unit,
            onValueChange = {},
            modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true),
            readOnly = true,
            label = { Text("Unit") },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            baseUnits.forEach { baseUnit ->
                DropdownMenuItem(
                    text = { Text(text = unitDisplay?.get(baseUnit) ?: baseUnit) },
                    onClick = {
                        onValueChange(baseUnit)
                        expanded = false
                    }
                )
            }
        }
    }
}