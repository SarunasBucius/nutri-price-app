package com.github.sarunasbucius.nutriprice.feature.recipe.insertRecipe

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.sarunasbucius.nutriprice.core.design.component.NutriPriceCircularProgress
import com.github.sarunasbucius.nutriprice.core.design.component.UnitDropdown
import com.github.sarunasbucius.nutriprice.core.navigation.currentComposeNavigator

@Composable
fun InsertRecipeScreen(
    insertRecipeViewModel: InsertRecipeViewModel = hiltViewModel()
) {
    val uiState = insertRecipeViewModel.uiState
    val composeNavigator = currentComposeNavigator
    val productList by insertRecipeViewModel.productList.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding())
    ) {
        RecipeDetailsSection(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            uiState,
            insertRecipeViewModel
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            text = "Ingredients",
        )

        uiState.ingredients.forEachIndexed { index, ingredient ->
            IngredientSection(
                ingredient = ingredient,
                updateIngredient = { insertRecipeViewModel.updateIngredient(it, index) },
                removeIngredient = { insertRecipeViewModel.removeIngredient(index) },
                ingredientsNumber = uiState.ingredients.size,
                productList = productList,
                isProductListLoading = uiState.isLoading
            )
        }

        uiState.errors.forEach {
            Text(text = it, color = Color.Red)
        }

        Button(onClick = { insertRecipeViewModel.insertRecipe { composeNavigator.navigateUp() } }) {
            Text(text = "Insert recipe")
        }
    }
}

@Composable
fun RecipeDetailsSection(
    modifier: Modifier = Modifier,
    uiState: InsertRecipeUiState,
    insertRecipeViewModel: InsertRecipeViewModel
) {
    Text(
        modifier = modifier.padding(8.dp),
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
}

@Composable
fun IngredientSection(
    ingredient: IngredientUi,
    updateIngredient: (IngredientUi) -> Unit,
    removeIngredient: () -> Unit,
    ingredientsNumber: Int,
    productList: List<String>,
    isProductListLoading: Boolean
) {
    Row(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
    ) {
        FilterableProductDropdown(
            modifier = Modifier.weight(1f),
            productList = productList,
            ingredient = ingredient,
            updateIngredient = { updateIngredient(it) },
            isLoading = isProductListLoading
        )
        if (ingredientsNumber > 1) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { removeIngredient() }
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
                updateIngredient(ingredient.copy(amount = it))
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),

            label = { Text("Amount") })

        UnitDropdown(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            unit = ingredient.unit,
            onValueChange = {
                updateIngredient(
                    ingredient.copy(unit = it)
                )
            }
        )
    }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = ingredient.notes,
        onValueChange = {
            updateIngredient(ingredient.copy(notes = it))
        },
        label = { Text("Notes") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
    )

    Spacer(modifier = Modifier.height(16.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterableProductDropdown(
    modifier: Modifier = Modifier,
    productList: List<String>,
    ingredient: IngredientUi,
    updateIngredient: (IngredientUi) -> Unit,
    isLoading: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            value = ingredient.name,
            onValueChange = {
                updateIngredient(ingredient.copy(name = it))
                expanded = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, true),

            label = { Text("Product") },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            if (isLoading) {
                DropdownMenuItem(
                    text = { Box(modifier = Modifier.fillMaxSize()) { NutriPriceCircularProgress() } },
                    onClick = { }
                )
            }
            productList.filter { it.contains(ingredient.name, ignoreCase = true) }.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        updateIngredient(ingredient.copy(name = it))
                        expanded = false
                    }
                )
            }
        }
    }
}