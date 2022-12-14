package com.example.uiWebCourse.models.modelForAdmin

import kotlinx.serialization.Serializable
import kotlinx.uuid.UUID

@Serializable
data class CreateCourseModel(
    val name: String,
    val description: String?
)

@Serializable
data class PutCourseModel(
    val courseInfoId: UUID,
    val name: String?,
    val description: String? = null
)