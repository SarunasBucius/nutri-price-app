package com.github.sarunasbucius.nutriprice.feature.product.editNutritionalValue

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.github.sarunasbucius.nutriprice.core.navigation.AppComposeNavigator
import com.github.sarunasbucius.nutriprice.core.navigation.NutriPriceScreen
import com.github.sarunasbucius.nutriprice.core.navigation.model.NutritionalValueNav
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarController
import com.github.sarunasbucius.nutriprice.core.snackbar.SnackbarEvent
import com.github.sarunasbucius.nutriprice.feature.product.common.model.NutritionalValueUi
import com.github.sarunasbucius.nutriprice.graphql.UpdateNutritionalValueMutation
import com.github.sarunasbucius.nutriprice.graphql.type.NutritionalValueInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditNutritionalValueUiState(
    val nutritionalValue: NutritionalValueUi = NutritionalValueUi(),
    val errors: List<String> = listOf(),
)


@HiltViewModel
class EditNutritionalValueViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val apolloClient: ApolloClient,
    private val navigation: AppComposeNavigator<NutriPriceScreen>,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    val productId = savedStateHandle["productId"] ?: ""
    val varietyName = savedStateHandle["varietyName"] ?: ""
    val nutritionalValue: NutritionalValueNav? = savedStateHandle["nutritionalValue"]
    var uiState by mutableStateOf(EditNutritionalValueUiState())
        private set

    init {
        if (varietyName.isEmpty() || nutritionalValue == null || productId.isEmpty()) {
            viewModelScope.launch {
                SnackbarController.sendEvent(SnackbarEvent("ERROR: something went wrong"))
                navigation.navigateUp()
            }
        }
        updateNutritionalValue(NutritionalValueUi.fromApiModel(nutritionalValue!!))
    }

    fun updateNutritionalValue(nv: NutritionalValueUi) {
        uiState = uiState.copy(nutritionalValue = nv)
    }

    fun submit() {
        val errors = mutableListOf<String>()
        errors.addAll(uiState.nutritionalValue.validate())
        uiState = uiState.copy(errors = errors)

        if (errors.isNotEmpty()) {
            return
        }
        viewModelScope.launch(ioDispatcher) {
            val result = apolloClient.mutation(
                UpdateNutritionalValueMutation(
                    id = productId,
                    varietyName = varietyName,
                    input = NutritionalValueInput(
                        unit = uiState.nutritionalValue.unit,
                        energyValueKcal = uiState.nutritionalValue.energyValueKcal.toDoubleOrNull()
                            ?: 0.0,
                        fat = uiState.nutritionalValue.fat.toDoubleOrNull() ?: 0.0,
                        saturatedFat = uiState.nutritionalValue.saturatedFat.toDoubleOrNull()
                            ?: 0.0,
                        carbohydrate = uiState.nutritionalValue.carbohydrate.toDoubleOrNull()
                            ?: 0.0,
                        carbohydrateSugars = uiState.nutritionalValue.carbohydrateSugars.toDoubleOrNull()
                            ?: 0.0,
                        fibre = uiState.nutritionalValue.fibre.toDoubleOrNull() ?: 0.0,
                        protein = uiState.nutritionalValue.protein.toDoubleOrNull() ?: 0.0,
                        salt = uiState.nutritionalValue.salt.toDoubleOrNull() ?: 0.0
                    ),
                )
            ).execute()
            if (result.hasErrors()) {
                Log.e("EditNutritionalValueViewModel", "submit: ${result.errors}")
                errors.add("Failed to update nutritional value")
                uiState = uiState.copy(errors = errors)
            } else {
                SnackbarController.sendEvent(SnackbarEvent("Updated successfully"))
                navigation.navigateUp()
            }
        }
    }
}