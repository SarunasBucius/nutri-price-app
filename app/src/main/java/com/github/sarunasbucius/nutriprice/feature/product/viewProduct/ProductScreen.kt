package com.github.sarunasbucius.nutriprice.feature.product.viewProduct

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sarunasbucius.nutriprice.core.navigation.LocalBackStack
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.core.navigation.model.NutritionalValueNav
import com.github.sarunasbucius.nutriprice.core.navigation.model.PurchaseDetailsNav
import com.github.sarunasbucius.nutriprice.graphql.ProductAggregateQuery

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(viewModel: ProductViewModel = hiltViewModel()) {
    PullToRefreshBox(
        isRefreshing = viewModel.uiState.isLoading,
        onRefresh = { viewModel.fetchProduct() }
    ) {
        if (!viewModel.uiState.isLoading) {
            ProductDetails(uiState = viewModel.uiState) {
                viewModel.selectVariety(it)
            }
        }
    }
}

@Composable
fun ProductDetails(uiState: ProductAggregateUi, onVarietySelected: (String) -> Unit) {
    val navigator = LocalBackStack.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding())
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            text = "Product details"
        )

        Text(
            text = "Product name",
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = uiState.productAggregate.name
        )

        Text(
            text = "Product variety",
            style = MaterialTheme.typography.labelSmall,
        )
        ProductVarietyDropdown(
            selectedVarietyName = uiState.selectedVariety.varietyName,
            varieties = uiState.productAggregate.varieties,
            onVarietySelected = onVarietySelected
        )

        Icon(
            modifier = Modifier
                .clickable(onClick = {
                    navigator.add(
                        NutriPriceScreen.EditProductName(
                            uiState.productId,
                            uiState.productAggregate.name,
                            uiState.selectedVariety.varietyName,
                        )
                    )
                }),
            tint = MaterialTheme.colorScheme.primary,
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit product details"
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            text = "Nutritional values"
        )

        if (uiState.selectedVariety.nutritionalValue != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextWithLabel(
                    modifier = Modifier.weight(1f),
                    label = "Unit",
                    text = uiState.selectedVariety.nutritionalValue.unit
                )
                TextWithLabel(
                    modifier = Modifier.weight(1f),
                    label = "Energy value (kcal)",
                    text = uiState.selectedVariety.nutritionalValue.energyValueKcal.toString()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextWithLabel(
                    modifier = Modifier.weight(1f),
                    label = "Fat",
                    text = uiState.selectedVariety.nutritionalValue.fat.toString()
                )
                TextWithLabel(
                    modifier = Modifier.weight(1f),
                    label = "Saturated fat",
                    text = uiState.selectedVariety.nutritionalValue.saturatedFat.toString()
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextWithLabel(
                    modifier = Modifier.weight(1f),
                    label = "Carbohydrate",
                    text = uiState.selectedVariety.nutritionalValue.carbohydrate.toString()
                )
                TextWithLabel(
                    modifier = Modifier.weight(1f),
                    label = "Sugars",
                    text = uiState.selectedVariety.nutritionalValue.carbohydrateSugars.toString()
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextWithLabel(
                    modifier = Modifier.weight(1f),
                    label = "Fibre",
                    text = uiState.selectedVariety.nutritionalValue.fibre.toString()
                )
                TextWithLabel(
                    modifier = Modifier.weight(1f),
                    label = "Protein",
                    text = uiState.selectedVariety.nutritionalValue.protein.toString()
                )
                TextWithLabel(
                    modifier = Modifier.weight(1f),
                    label = "Salt",
                    text = uiState.selectedVariety.nutritionalValue.salt.toString()
                )
            }

            Icon(
                modifier = Modifier
                    .clickable(onClick = {
                        navigator.add(
                            NutriPriceScreen.EditNutritionalValue(
                                productId = uiState.productId,
                                varietyName = uiState.selectedVariety.varietyName,
                                nutritionalValue = NutritionalValueNav(
                                    unit = uiState.selectedVariety.nutritionalValue.unit,
                                    energyValueKcal = uiState.selectedVariety.nutritionalValue.energyValueKcal,
                                    fat = uiState.selectedVariety.nutritionalValue.fat,
                                    saturatedFat = uiState.selectedVariety.nutritionalValue.saturatedFat,
                                    carbohydrate = uiState.selectedVariety.nutritionalValue.carbohydrate,
                                    carbohydrateSugars = uiState.selectedVariety.nutritionalValue.carbohydrateSugars,
                                    fibre = uiState.selectedVariety.nutritionalValue.fibre,
                                    protein = uiState.selectedVariety.nutritionalValue.protein,
                                    salt = uiState.selectedVariety.nutritionalValue.salt
                                )
                            )
                        )
                    }),
                tint = MaterialTheme.colorScheme.primary,
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit nutritional values"
            )
        } else {
            Button(onClick = {
                navigator.add(
                    NutriPriceScreen.EditNutritionalValue(
                        productId = uiState.productId,
                        varietyName = uiState.selectedVariety.varietyName,
                        nutritionalValue = NutritionalValueNav()
                    )
                )
            }) {
                Text(text = "Set nutritional values")
            }
        }

        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            text = "Purchase details"
        )
        uiState.selectedVariety.purchases.forEachIndexed { index, purchasedProduct ->
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextWithLabel(
                    modifier = Modifier.weight(1f),
                    label = "Date",
                    text = purchasedProduct.date
                )
                TextWithLabel(
                    modifier = Modifier.weight(1f),
                    label = "Retailer",
                    text = purchasedProduct.retailer
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextWithLabel(
                    modifier = Modifier.weight(1f),
                    label = "Price",
                    text = purchasedProduct.price.toString()
                )
                TextWithLabel(
                    modifier = Modifier.weight(1f),
                    label = "Amount",
                    text = "${purchasedProduct.quantity} ${purchasedProduct.unit}"
                )
            }
            if (!purchasedProduct.notes.isEmpty()) {
                TextWithLabel(
                    label = "Notes",
                    text = purchasedProduct.notes
                )
            }
            Icon(
                modifier = Modifier
                    .clickable(onClick = {
                        navigator.add(
                            NutriPriceScreen.EditPurchase(
                                purchaseDetails = PurchaseDetailsNav(
                                    id = purchasedProduct.id,
                                    date = purchasedProduct.date,
                                    retailer = purchasedProduct.retailer,
                                    price = purchasedProduct.price,
                                    amount = purchasedProduct.quantity,
                                    unit = purchasedProduct.unit,
                                    notes = purchasedProduct.notes
                                )
                            )
                        )
                    }),
                tint = MaterialTheme.colorScheme.primary,
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit purchase details"
            )
            if (index != uiState.selectedVariety.purchases.lastIndex) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            }
        }
    }
}

@Composable
fun TextWithLabel(modifier: Modifier = Modifier, label: String, text: String) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = text
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductVarietyDropdown(
    selectedVarietyName: String,
    varieties: List<ProductAggregateQuery.Variety>,
    onVarietySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, true),
        ) {
            Text(
                text = selectedVarietyName,
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Show varieties"
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            varieties.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.varietyName) },
                    onClick = {
                        onVarietySelected(item.varietyName)
                        expanded = false
                    }
                )
            }
        }
    }
}