package com.github.sarunasbucius.nutriprice.feature.recipeList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.sarunasbucius.nutriprice.core.design.component.NutriPriceCircularProgress

@Composable
fun RecipeListScreen(recipeListViewModel: RecipeListViewModel = hiltViewModel()) {
    val uiState by recipeListViewModel.uiState.collectAsStateWithLifecycle()
    val recipeList by recipeListViewModel.recipeList.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (uiState is RecipeListUiState.Loading) {
            Box(modifier = Modifier.fillMaxSize()) { NutriPriceCircularProgress() }
        } else {
            recipeList.forEach {
                Text(text = it)
            }
        }
    }
}