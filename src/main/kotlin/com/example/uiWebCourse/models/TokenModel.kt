package com.example.uiWebCourse.models

import kotlinx.serialization.Serializable

@Serializable
data class TokensModel(
    val access: String,
    val refresh: String
)