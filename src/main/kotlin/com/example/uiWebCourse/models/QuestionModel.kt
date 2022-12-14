package com.example.uiWebCourse.models

import kotlinx.serialization.Serializable
import kotlinx.uuid.UUID

@Serializable
data class QuestionsInfoModel(
    val questionInfoId: UUID,
    val question: String,
    val courseInfoId: UUID,
    val rightAnswerId: UUID,
    val listAnswer: List<AnswerModel>
)