package com.github.sarunasbucius.nutriprice.feature.insertRecipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.github.sarunasbucius.nutriprice.core.model.QuantityUnit
import com.github.sarunasbucius.nutriprice.core.navigation.currentComposeNavigator

@Composable
fun InsertRecipeScreen(
    insertRecipeViewModel: InsertRecipeViewModel = hiltViewModel()
) {
    val uiState = insertRecipeViewModel.uiState
    val composeNavigator = currentComposeNavigator
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding())
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            text = "Recipe details",
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = uiState.recipeName,
            onValueChange = { insertRecipeViewModel.updateName(it) },
            label = { Text("Recipe name") },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = uiState.notes,
            onValueChange = { insertRecipeViewModel.updateNotes(it) },
            label = { Text("Notes") },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )

        uiState.steps.forEachIndexed { index, step ->
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    modifier = Modifier.weight(1f),
                    value = step,
                    onValueChange = {
                        insertRecipeViewModel.updateStep(it, index)
                    },
                    label = { Text("Step ${index + 1}") },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                )
                if (uiState.steps.size > 1) {
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        onClick = { insertRecipeViewModel.removeStep(index) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Remove step"
                        )
                    }
                }
            }
        }

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            text = "Ingredients",
        )

        uiState.ingredients.forEachIndexed { index, ingredient ->
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    modifier = Modifier.weight(1f),
                    value = ingredient.name,
                    onValueChange = {
                        insertRecipeViewModel.updateIngredient(ingredient.copy(name = it), index)
                    },
                    label = { Text("Product") },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                )
                if (uiState.ingredients.size > 1) {
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        onClick = { insertRecipeViewModel.removeIngredient(index) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Remove ingredient"
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    value = ingredient.amount,
                    onValueChange = {
                        insertRecipeViewModel.updateIngredient(ingredient.copy(amount = it), index)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),

                    label = { Text("Amount") })

                UnitDropdown(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    unit = ingredient.unit,
                    onValueChange = {
                        insertRecipeViewModel.updateIngredient(
                            ingredient.copy(
                                unit = it as? QuantityUnit ?: QuantityUnit.UNSPECIFIED
                            ), index
                        )
                    }
                )
            }

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = ingredient.notes,
                onValueChange = {
                    insertRecipeViewModel.updateIngredient(
                        ingredient.copy(notes = it),
                        index
                    )
                },
                label = { Text("Notes") },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        uiState.errors.forEach {
            Text(text = it, color = androidx.compose.ui.graphics.Color.Red)
        }

        Button(onClick = { insertRecipeViewModel.insertRecipe { composeNavigator.navigateUp() } }) {
            Text(text = "Insert recipe")
        }
    }
}