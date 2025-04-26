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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.github.sarunasbucius.nutriprice.core.design.theme.NutriPriceTheme
import com.github.sarunasbucius.nutriprice.core.navigation.AppComposeNavigator
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarController
import com.github.sarunasbucius.nutriprice.navigation.NutriPriceNavHost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun NutriPriceMain(composeNavigator: AppComposeNavigator<NutriPriceScreen>) {
    NutriPriceTheme {
        val navHostController = rememberNavController()
        val snackbarHost = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(Unit) {
            launch {
                composeNavigator.handleNavigationCommands(navHostController)
            }
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
                NutriPriceNavHost(navHostController = navHostController)
            }
        }
    }
}