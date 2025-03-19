package com.github.sarunasbucius.nutriprice.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val name: String,
    val ingredients: List<Ingredient>,
    val steps: List<String>,
    val notes: String,
)

@Serializable
data class Ingredient(
    val name: String,
    val amount: Double?,
    val unit: String,
    val notes: String
)