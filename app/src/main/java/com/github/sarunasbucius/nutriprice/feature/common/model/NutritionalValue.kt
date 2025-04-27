package com.github.sarunasbucius.nutriprice.feature.common.model

import com.github.sarunasbucius.nutriprice.core.model.NutritionalValue

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

    companion object {
        fun fromApiModel(nutritionalValue: NutritionalValue): NutritionalValueUi {
            return NutritionalValueUi(
                unit = nutritionalValue.unit ?: "",
                energyValueKcal = nutritionalValue.energyValueKcal?.takeIf { it != 0.0 }?.toString()
                    ?: "",
                fat = nutritionalValue.fat?.takeIf { it != 0.0 }?.toString() ?: "",
                saturatedFat = nutritionalValue.saturatedFat?.takeIf { it != 0.0 }?.toString()
                    ?: "",
                carbohydrate = nutritionalValue.carbohydrate?.takeIf { it != 0.0 }?.toString()
                    ?: "",
                carbohydrateSugars = nutritionalValue.carbohydrateSugars?.takeIf { it != 0.0 }
                    ?.toString() ?: "",
                fibre = nutritionalValue.fibre?.takeIf { it != 0.0 }?.toString() ?: "",
                protein = nutritionalValue.protein?.takeIf { it != 0.0 }?.toString() ?: "",
                salt = nutritionalValue.salt?.takeIf { it != 0.0 }?.toString() ?: "",
            )
        }
    }
}