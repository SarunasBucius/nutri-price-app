package com.github.sarunasbucius.nutriprice.feature.insertProduct

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sarunasbucius.nutriprice.core.model.NewProduct
import com.github.sarunasbucius.nutriprice.core.model.NutritionalValue
import com.github.sarunasbucius.nutriprice.core.model.NutritionalValueUnit
import com.github.sarunasbucius.nutriprice.core.model.QuantityUnit
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
    val unit: QuantityUnit = QuantityUnit.UNSPECIFIED,
    val notes: String = "",
    val nutritionalValues: NutritionalValueUi = NutritionalValueUi(),
    val errors: List<String> = emptyList(),
)

data class NutritionalValueUi(
    val unit: NutritionalValueUnit = NutritionalValueUnit.UNSPECIFIED,
    val energyValueKcal: String = "",
    val fat: String = "",
    val saturatedFat: String = "",
    val carbohydrate: String = "",
    val carbohydrateSugars: String = "",
    val fibre: String = "",
    val protein: String = "",
    val salt: String = "",
) {
    fun validate(): List<String> {
        val errors = mutableListOf<String>()

        fun validateNumericField(value: String, fieldName: String) {
            if (value.isNotEmpty() && value.toDoubleOrNull() == null) {
                errors.add("$fieldName must be a number")
            }
        }

        validateNumericField(energyValueKcal, "Energy value (kcal)")
        validateNumericField(fat, "Fat")
        validateNumericField(saturatedFat, "Saturated fat")
        validateNumericField(carbohydrate, "Carbohydrate")
        validateNumericField(carbohydrateSugars, "Carbohydrate sugars")
        validateNumericField(fibre, "Fibre")
        validateNumericField(protein, "Protein")
        validateNumericField(salt, "Salt")

        return errors
    }

    fun toApiModel(): NutritionalValue {
        return NutritionalValue(
            unit = unit,
            energyValueKcal = energyValueKcal.toDoubleOrNull(),
            fat = fat.toDoubleOrNull(),
            saturatedFat = saturatedFat.toDoubleOrNull(),
            carbohydrate = carbohydrate.toDoubleOrNull(),
            carbohydrateSugars = carbohydrateSugars.toDoubleOrNull(),
            fibre = fibre.toDoubleOrNull(),
            protein = protein.toDoubleOrNull(),
            salt = salt.toDoubleOrNull()
        )
    }
}

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

    fun updateUnit(unit: QuantityUnit) {
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

        errors.addAll(uiState.nutritionalValues.validate())

        validateNumericField(uiState.price, "Price")
        validateNumericField(uiState.amount, "Amount")

        uiState = uiState.copy(errors = errors)
        if (errors.isNotEmpty()) {
            return
        }

        val newProduct = NewProduct(
            name = uiState.productName,
            price = uiState.price.toDoubleOrNull(),
            amount = uiState.amount.toDoubleOrNull(),
            unit = uiState.unit,
            notes = uiState.notes,
            nutritionalValues = uiState.nutritionalValues.toApiModel(),
        )
        viewModelScope.launch(ioDispatcher) {
            val result = nutriPriceClient.insertProduct(newProduct)
            result.onSuccess {
                onProductInserted()
            }.onError {
                uiState = uiState.copy(errors = listOf("Something went wrong"))
            }
        }
    }
}