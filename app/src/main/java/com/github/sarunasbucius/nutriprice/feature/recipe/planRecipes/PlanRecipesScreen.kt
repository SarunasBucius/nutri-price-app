package com.github.sarunasbucius.nutriprice.feature.recipe.planRecipes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.sarunasbucius.nutriprice.core.design.component.DatePicker

@Composable
fun PlanRecipesScreen(viewModel: PlanRecipesViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState
    val recipeList by viewModel.recipeList.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        DatePicker(
            label = "Preparation date",
            date = uiState.date,
            onDateChange = { viewModel.updateDate(it) }
        )

        uiState.plannedRecipes.forEachIndexed { index, planRecipeUi ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)) {
                FilterableRecipeDropdown(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    recipeList = recipeList,
                    recipeName = planRecipeUi.recipeName,
                    updateRecipeName = { viewModel.updateRecipeName(it, index) }
                )
                TextField(
                    modifier = Modifier
                        .weight(0.3f)
                        .padding(start = 4.dp),
                    value = planRecipeUi.portion,
                    onValueChange = { viewModel.updatePortion(it, index) },
                    label = { Text("Portions") },
                )
            }
        }
        uiState.errors.forEach {
            Text(text = it)
        }
        Button(onClick = { viewModel.submit() }) {
            Text(text = "Submit")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterableRecipeDropdown(
    modifier: Modifier = Modifier,
    recipeList: List<String>,
    recipeName: String,
    updateRecipeName: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            value = recipeName,
            onValueChange = {
                updateRecipeName(it)
                expanded = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, true),

            label = { Text("Recipe") },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            recipeList.filter { it.contains(recipeName, ignoreCase = true) }.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        updateRecipeName(it)
                        expanded = false
                    }
                )
            }
        }
    }
}