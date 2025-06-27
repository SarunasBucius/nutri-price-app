package com.github.sarunasbucius.nutriprice.core.navigation

import androidx.compose.runtime.compositionLocalOf

val LocalBackStack = compositionLocalOf<MutableList<NutriPriceScreen>> {
    error("No back stack provided")
}