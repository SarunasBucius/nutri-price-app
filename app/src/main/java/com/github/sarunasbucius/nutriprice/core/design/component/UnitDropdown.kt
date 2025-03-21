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
import com.github.sarunasbucius.nutriprice.core.model.DisplayableUnit
import com.github.sarunasbucius.nutriprice.core.model.QuantityUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitDropdown(
    modifier: Modifier = Modifier,
    unit: DisplayableUnit,
    units: List<DisplayableUnit> = QuantityUnit.entries,
    onValueChange: (DisplayableUnit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        TextField(
            value = unit.display,
            onValueChange = { onValueChange },
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, true),
            readOnly = true,
            label = { Text("Unit") },
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            units.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.display) },
                    onClick = {
                        onValueChange(item)
                        expanded = false
                    }
                )
            }
        }
    }
}