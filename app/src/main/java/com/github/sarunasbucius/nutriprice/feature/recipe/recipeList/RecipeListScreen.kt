package com.github.sarunasbucius.nutriprice.feature.recipe.recipeList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.github.sarunasbucius.nutriprice.core.design.component.SearchInList
import com.github.sarunasbucius.nutriprice.core.navigation.LocalBackStack
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen

@Composable
fun RecipeListScreen(viewModel: RecipeListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filteredRecipeList by viewModel.filteredRecipeList.collectAsStateWithLifecycle()

    val composeNavigator = LocalBackStack.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) { NutriPriceCircularProgress() }
        } else {
            SearchInList(
                input = uiState.searchInput,
                onInputChange = { viewModel.onSearchInputChanged(it) }
            )
            filteredRecipeList.forEach {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            composeNavigator.add(NutriPriceScreen.Recipe(it))
                        }),
                    text = it,
                )
            }
        }
    }
}