package com.github.sarunasbucius.nutriprice.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.core.navigation.currentComposeNavigator

@Composable
fun HomeScreen() {
    val composeNavigator = currentComposeNavigator
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            composeNavigator.navigate(NutriPriceScreen.ProductList)
        }) {
            Text(text = "Product list")
        }
        Button(onClick = {
            composeNavigator.navigate(NutriPriceScreen.InsertProduct)
        }) {
            Text(text = "Insert Product")
        }
    }
}