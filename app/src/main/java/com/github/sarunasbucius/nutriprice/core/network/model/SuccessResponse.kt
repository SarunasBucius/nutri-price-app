package com.github.sarunasbucius.nutriprice.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SuccessResponse(
    val message: String
)