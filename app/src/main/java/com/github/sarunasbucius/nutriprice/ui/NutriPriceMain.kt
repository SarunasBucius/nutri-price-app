package com.github.sarunasbucius.nutriprice.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.github.sarunasbucius.nutriprice.core.design.theme.NutriPriceTheme
import com.github.sarunasbucius.nutriprice.core.navigation.LocalBackStack
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarController
import com.github.sarunasbucius.nutriprice.feature.home.HomeScreen
import com.github.sarunasbucius.nutriprice.feature.product.editNutritionalValue.EditNutritionalValueScreen
import com.github.sarunasbucius.nutriprice.feature.product.editNutritionalValue.EditNutritionalValueViewModel
import com.github.sarunasbucius.nutriprice.feature.product.editProductName.EditProductNameScreen
import com.github.sarunasbucius.nutriprice.feature.product.editProductName.EditProductNameViewModel
import com.github.sarunasbucius.nutriprice.feature.product.editPurchase.EditPurchaseScreen
import com.github.sarunasbucius.nutriprice.feature.product.editPurchase.EditPurchaseViewModel
import com.github.sarunasbucius.nutriprice.feature.product.insertProduct.InsertProductScreen
import com.github.sarunasbucius.nutriprice.feature.product.productList.ProductListScreen
import com.github.sarunasbucius.nutriprice.feature.product.viewProduct.ProductScreen
import com.github.sarunasbucius.nutriprice.feature.product.viewProduct.ProductViewModel
import com.github.sarunasbucius.nutriprice.feature.recipe.editPreparedRecipe.EditPreparedRecipeScreen
import com.github.sarunasbucius.nutriprice.feature.recipe.editPreparedRecipe.EditPreparedRecipeViewModel
import com.github.sarunasbucius.nutriprice.feature.recipe.planRecipes.PlanRecipesScreen
import com.github.sarunasbucius.nutriprice.feature.recipe.planRecipes.PlanRecipesViewModel
import com.github.sarunasbucius.nutriprice.feature.recipe.preparedRecipes.PreparedRecipesScreen
import com.github.sarunasbucius.nutriprice.feature.recipe.recipeList.RecipeListScreen
import com.github.sarunasbucius.nutriprice.feature.recipe.upsertRecipe.UpsertRecipeScreen
import com.github.sarunasbucius.nutriprice.feature.recipe.upsertRecipe.UpsertRecipeViewModel
import com.github.sarunasbucius.nutriprice.feature.recipe.viewPreparedRecipe.PreparedRecipeScreen
import com.github.sarunasbucius.nutriprice.feature.recipe.viewPreparedRecipe.ViewPreparedRecipeViewModel
import com.github.sarunasbucius.nutriprice.feature.recipe.viewRecipe.RecipeScreen
import com.github.sarunasbucius.nutriprice.feature.recipe.viewRecipe.RecipeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun NutriPriceMain() {
    NutriPriceTheme {
        val snackbarHost = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val lifecycleOwner = LocalLifecycleOwner.current
        val backStack = LocalBackStack.current
        LaunchedEffect(Unit) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main.immediate) {

                    SnackbarController.events.collect { event ->
                        scope.launch {
                            snackbarHost.currentSnackbarData?.dismiss()
                            snackbarHost.showSnackbar(
                                message = event.message,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            }
        }
        val focusManager = LocalFocusManager.current
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHost,
                    snackbar = { snackbarData ->
                        val isError = snackbarData.visuals.message.startsWith("ERROR")

                        Snackbar(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            snackbarData = snackbarData
                        )
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }) {
                NavDisplay(
                    entryDecorators = listOf(
                        rememberSavedStateNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = { route ->
                        when (route) {
                            is NutriPriceScreen.Home -> NavEntry(route) {
                                HomeScreen()
                            }

                            is NutriPriceScreen.ProductList -> NavEntry(route) {
                                ProductListScreen()
                            }

                            is NutriPriceScreen.InsertProduct -> NavEntry(route) {
                                InsertProductScreen()
                            }

                            is NutriPriceScreen.RecipeList -> NavEntry(route) {
                                RecipeListScreen()
                            }

                            is NutriPriceScreen.UpsertRecipe -> NavEntry(route) {
                                val viewModel =
                                    hiltViewModel<UpsertRecipeViewModel, UpsertRecipeViewModel.Factory>(
                                        key = "UpsertRecipe_$route",
                                        creationCallback = { factory ->
                                            factory.create(route)
                                        }
                                    )
                                UpsertRecipeScreen(upsertRecipeViewModel = viewModel)
                            }

                            is NutriPriceScreen.Product -> NavEntry(route) {
                                val viewModel =
                                    hiltViewModel<ProductViewModel, ProductViewModel.Factory>(
                                        // Without key, entering to different product shows details from first visited product
                                        key = "Product_$route",
                                        creationCallback = { factory ->
                                            factory.create(route)
                                        }
                                    )
                                ProductScreen(viewModel = viewModel)
                            }

                            is NutriPriceScreen.EditProductName -> NavEntry(route) {
                                val viewModel =
                                    hiltViewModel<EditProductNameViewModel, EditProductNameViewModel.Factory>(
                                        key = "EditProductName_$route",
                                        creationCallback = { factory ->
                                            factory.create(route)
                                        }
                                    )
                                EditProductNameScreen(viewModel = viewModel)
                            }

                            is NutriPriceScreen.EditPurchase -> NavEntry(route) {
                                val viewModel =
                                    hiltViewModel<EditPurchaseViewModel, EditPurchaseViewModel.Factory>(
                                        key = "EditPurchase_$route",
                                        creationCallback = { factory ->
                                            factory.create(route)
                                        }
                                    )
                                EditPurchaseScreen(viewModel = viewModel)
                            }

                            is NutriPriceScreen.EditNutritionalValue -> NavEntry(route) {
                                val viewModel =
                                    hiltViewModel<EditNutritionalValueViewModel, EditNutritionalValueViewModel.Factory>(
                                        key = "EditNutritionalValue_$route",
                                        creationCallback = { factory ->
                                            factory.create(route)
                                        }
                                    )
                                EditNutritionalValueScreen(viewModel = viewModel)
                            }

                            is NutriPriceScreen.Recipe -> NavEntry(route) {
                                val viewModel =
                                    hiltViewModel<RecipeViewModel, RecipeViewModel.Factory>(
                                        key = "Recipe_$route",
                                        creationCallback = { factory ->
                                            factory.create(route)
                                        }
                                    )
                                RecipeScreen(viewModel = viewModel)
                            }

                            is NutriPriceScreen.PreparedRecipes -> NavEntry(route) {
                                PreparedRecipesScreen()
                            }

                            is NutriPriceScreen.PlanRecipes -> NavEntry(route) {
                                val viewModel =
                                    hiltViewModel<PlanRecipesViewModel, PlanRecipesViewModel.Factory>(
                                        key = "PlanRecipes_$route",
                                        creationCallback = { factory ->
                                            factory.create(route)
                                        }
                                    )
                                PlanRecipesScreen(viewModel = viewModel)
                            }

                            is NutriPriceScreen.PreparedRecipe -> NavEntry(route) {
                                val viewModel =
                                    hiltViewModel<ViewPreparedRecipeViewModel, ViewPreparedRecipeViewModel.Factory>(
                                        key = "PreparedRecipe_$route",
                                        creationCallback = { factory ->
                                            factory.create(route)
                                        }
                                    )
                                PreparedRecipeScreen(viewModel = viewModel)
                            }

                            is NutriPriceScreen.EditPreparedRecipe -> NavEntry(route) {
                                val viewModel =
                                    hiltViewModel<EditPreparedRecipeViewModel, EditPreparedRecipeViewModel.Factory>(
                                        key = "EditPreparedRecipe_$route",
                                        creationCallback = { factory ->
                                            factory.create(route)
                                        }
                                    )
                                EditPreparedRecipeScreen(viewModel = viewModel)
                            }
                        }
                    },
                )
            }
        }
    }
}