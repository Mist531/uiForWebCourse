package com.example.uiWebCourse.models

import kotlinx.serialization.Serializable
import kotlinx.uuid.UUID

@Serializable
data class RegisterUserModel(
    val login: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String? = null
)

@Serializable
data class GetUserModel(
    val userId: UUID,
    val firstName: String,
    val lastName: String,
    val patronymic: String? = null
)

@Serializable
data class LoginModel(
    val login: String,
    val password: String
)
