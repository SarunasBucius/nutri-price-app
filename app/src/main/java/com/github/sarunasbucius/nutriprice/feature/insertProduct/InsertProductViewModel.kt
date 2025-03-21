package com.github.sarunasbucius.nutriprice.feature.insertProduct

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sarunasbucius.nutriprice.core.model.NutritionalValue
import com.github.sarunasbucius.nutriprice.core.model.Product
import com.github.sarunasbucius.nutriprice.core.network.Dispatcher
import com.github.sarunasbucius.nutriprice.core.network.NutriPriceAppDispatchers
import com.github.sarunasbucius.nutriprice.core.network.service.NutriPriceClient
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InsertProductUiState(
    val productName: String = "",
    val price: String = "",
    val amount: String = "",
    val unit: String = "",
    val notes: String = "",
    val nutritionalValues: NutritionalValueUi = NutritionalValueUi(),
    val errors: List<String> = emptyList(),
)

data class NutritionalValueUi(
    val unit: String = "",
    val energyValueKcal: String = "",
    val fat: String = "",
    val saturatedFat: String = "",
    val carbohydrate: String = "",
    val carbohydrateSugars: String = "",
    val fibre: String = "",
    val protein: String = "",
    val salt: String = "",
)

@HiltViewModel
class InsertProductViewModel @Inject constructor(
    private val nutriPriceClient: NutriPriceClient,
    @Dispatcher(NutriPriceAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    var uiState by mutableStateOf(InsertProductUiState())
        private set

    fun updateName(name: String) {
        uiState = uiState.copy(productName = name)
    }

    fun updatePrice(price: String) {
        uiState = uiState.copy(price = price)
    }

    fun updateAmount(amount: String) {
        uiState = uiState.copy(amount = amount)
    }

    fun updateUnit(unit: String) {
        uiState = uiState.copy(unit = unit)
    }

    fun updateNotes(notes: String) {
        uiState = uiState.copy(notes = notes)
    }

    fun updateNutritionalValue(nutritionalValue: NutritionalValueUi) {
        uiState = uiState.copy(nutritionalValues = nutritionalValue)
    }

    fun insertProduct(onProductInserted: () -> Unit) {
        val errors = mutableListOf<String>()
        if (uiState.productName.isEmpty()) {
            errors.add("Name cannot be empty")
        }

        fun validateNumericField(value: String, fieldName: String) {
            if (value.isNotEmpty() && value.toDoubleOrNull() == null) {
                errors.add("$fieldName must be a number")
            }
        }

        val nv = uiState.nutritionalValues
        validateNumericField(uiState.price, "Price")
        validateNumericField(uiState.amount, "Amount")
        validateNumericField(nv.energyValueKcal, "Energy value (kcal)")
        validateNumericField(nv.fat, "Fat")
        validateNumericField(nv.saturatedFat, "Saturated fat")
        validateNumericField(nv.carbohydrate, "Carbohydrate")
        validateNumericField(nv.carbohydrateSugars, "Carbohydrate sugars")
        validateNumericField(nv.fibre, "Fibre")
        validateNumericField(nv.protein, "Protein")
        validateNumericField(nv.salt, "Salt")

        uiState = uiState.copy(errors = errors)
        if (errors.isNotEmpty()) {
            return
        }

        val product = Product(
            name = uiState.productName,
            price = uiState.price.toDoubleOrNull(),
            amount = uiState.amount.toDoubleOrNull(),
            unit = uiState.unit,
            notes = uiState.notes,
            nutritionalValues = NutritionalValue(
                unit = uiState.nutritionalValues.unit,
                energyValueKcal = nv.energyValueKcal.toDoubleOrNull(),
                fat = nv.fat.toDoubleOrNull(),
                saturatedFat = nv.saturatedFat.toDoubleOrNull(),
                carbohydrate = nv.carbohydrate.toDoubleOrNull(),
                carbohydrateSugars = nv.carbohydrateSugars.toDoubleOrNull(),
                fibre = nv.fibre.toDoubleOrNull(),
                protein = nv.protein.toDoubleOrNull(),
                salt = nv.salt.toDoubleOrNull()
            ),
        )
        viewModelScope.launch(ioDispatcher) {
            val result = nutriPriceClient.insertProduct(product)
            result.onSuccess {
                onProductInserted()
            }.onError {
                uiState = uiState.copy(errors = listOf("Something went wrong"))
            }
        }
    }
}