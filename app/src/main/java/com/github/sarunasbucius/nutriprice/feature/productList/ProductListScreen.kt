package com.github.sarunasbucius.nutriprice.feature.productList

import androidx.compose.foundation.clickable
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
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.core.navigation.currentComposeNavigator

@Composable
fun ProductListScreen(productListViewModel: ProductListViewModel = hiltViewModel()) {
    val uiState by productListViewModel.uiState.collectAsStateWithLifecycle()
    val productList by productListViewModel.productList.collectAsStateWithLifecycle()
    val composeNavigator = currentComposeNavigator
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (uiState is ProductListUiState.Loading) {
            Box(modifier = Modifier.fillMaxSize()) { NutriPriceCircularProgress() }
        } else {
            productList.forEach {
                Text(
                    modifier = Modifier.clickable(onClick = {
                        composeNavigator.navigate(NutriPriceScreen.Product(it.id))
                    }),
                    text = it.name
                )
            }
        }
    }
}