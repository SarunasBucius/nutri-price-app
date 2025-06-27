package com.github.sarunasbucius.nutriprice.feature.recipe.preparedRecipes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.sarunasbucius.nutriprice.core.design.component.DatePicker
import com.github.sarunasbucius.nutriprice.core.navigation.LocalBackStack
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen

@Composable
fun PreparedRecipesScreen(viewModel: PreparedRecipesViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val composeNavigator = LocalBackStack.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        DatePicker(
            label = "Preparation date",
            date = uiState.value.date,
            onDateChange = { viewModel.updateDate(it) }
        )
        Button(onClick = {
            composeNavigator.add(NutriPriceScreen.PlanRecipes(uiState.value.date))
        }) {
            Text("Plan day's recipes")
        }

        uiState.value.preparedRecipes.forEach {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        composeNavigator.add(
                            NutriPriceScreen.PreparedRecipe(
                                it,
                                uiState.value.date
                            )
                        )
                    }),
                text = it,
            )
        }
    }
}