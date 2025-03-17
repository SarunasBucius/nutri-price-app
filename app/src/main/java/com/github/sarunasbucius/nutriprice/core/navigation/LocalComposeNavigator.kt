package com.github.sarunasbucius.nutriprice.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf


val LocalComposeNavigator: ProvidableCompositionLocal<AppComposeNavigator<NutriPriceScreen>> =
    compositionLocalOf {
        error(
            "No AppComposeNavigator provided!"
        )
    }

/**
 * Retrieves the current [AppComposeNavigator] at the call site's position in the hierarchy.
 */
val currentComposeNavigator: AppComposeNavigator<NutriPriceScreen>
    @Composable
    @ReadOnlyComposable
    get() = LocalComposeNavigator.current
