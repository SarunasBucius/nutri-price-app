package com.github.sarunasbucius.nutriprice.feature.product.editNutritionalValue

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sarunasbucius.nutriprice.feature.product.common.components.NutritionalValueInput

@Composable
fun EditNutritionalValueScreen(viewModel: EditNutritionalValueViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        NutritionalValueInput(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            nutritionalValue = viewModel.uiState.nutritionalValue,
            updateNutritionalValue = viewModel::updateNutritionalValue
        )
        viewModel.uiState.errors.forEach {
            Text(text = it, color = androidx.compose.ui.graphics.Color.Red)
        }
        Button(onClick = { viewModel.submit() }) {
            Text(text = "Submit")
        }
    }
}