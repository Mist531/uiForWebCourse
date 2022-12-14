package com.example.uiWebCourse.models

import kotlinx.serialization.Serializable
import kotlinx.uuid.UUID

@Serializable
data class AnswerModel(
    val answerInfoId: UUID,
    val questionInfoId: UUID,
    val answer: String
)