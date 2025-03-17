package com.github.sarunasbucius.nutriprice.core.navigation

import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import javax.inject.Inject


class NutriPriceComposeNavigator @Inject constructor() : AppComposeNavigator<NutriPriceScreen>() {

    override fun navigate(
        route: NutriPriceScreen,
        optionsBuilder: (NavOptionsBuilder.() -> Unit)?
    ) {
        val options = optionsBuilder?.let { navOptions(it) }
        navigationCommands.tryEmit(ComposeNavigationCommand.NavigateToRoute(route, options))
    }
}

