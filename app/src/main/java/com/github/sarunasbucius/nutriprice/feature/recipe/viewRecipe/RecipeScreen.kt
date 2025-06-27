package com.github.sarunasbucius.nutriprice.feature.recipe.viewRecipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sarunasbucius.nutriprice.core.design.component.NutriPriceCircularProgress
import com.github.sarunasbucius.nutriprice.core.navigation.LocalBackStack
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen

@Composable
fun RecipeScreen(viewModel: RecipeViewModel = hiltViewModel()) {
    if (viewModel.uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) { NutriPriceCircularProgress() }
    } else {
        RecipeDetails(viewModel.uiState)
    }
}

@Composable
fun RecipeDetails(uiState: RecipeUi) {
    val composeNavigator = LocalBackStack.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            text = "Recipe details"
        )
        Text(
            text = "Recipe name",
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = uiState.recipeName
        )

        if (uiState.recipe.notes.isNotEmpty()) {
            Text(
                text = "Notes",
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                text = uiState.recipe.notes
            )
        }

        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            text = "Ingredients"
        )
        uiState.recipe.ingredients.forEach {
            Row {
                Column(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = "Product",
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Text(
                        text = it.product
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = "Quantity",
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Text(
                        text = "${it.quantity} ${it.unit}"
                    )
                }
            }
            if (it.notes.isNotEmpty()) {
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = it.notes
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (uiState.recipe.steps.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Steps"
            )
            uiState.recipe.steps.forEachIndexed { index, it ->
                Text(
                    text = "${index + 1}: $it"
                )
            }
        }
        Icon(
            modifier = Modifier
                .clickable(onClick = {
                    composeNavigator.add(
                        NutriPriceScreen.UpsertRecipe(
                            uiState.recipeName
                        )
                    )
                }),
            tint = MaterialTheme.colorScheme.primary,
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit recipe details"
        )
    }
}