package com.github.sarunasbucius.nutriprice.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface DisplayableUnit {
    val display: String
}

@Serializable
enum class QuantityUnit(override val display: String) : DisplayableUnit {
    @SerialName("")
    UNSPECIFIED(""),

    @SerialName("g")
    GRAMS("g"),

    @SerialName("ml")
    MILLILITERS("ml"),

    @SerialName("pcs")
    PIECES("pcs");
}

@Serializable
enum class NutritionalValueUnit(override val display: String) : DisplayableUnit {
    @SerialName("")
    UNSPECIFIED(""),

    @SerialName("100 g")
    GRAMS("100 g"),

    @SerialName("100 ml")
    MILLILITERS("100 ml"),

    @SerialName("1 piece")
    PIECES("1 piece");
}