package com.example.uiWebCourse.models.modelForAdmin

import kotlinx.serialization.Serializable
import kotlinx.uuid.UUID

@Serializable
data class PutAnswerModel(
    val questionInfoId: UUID,
    val answerInfoId: UUID,
    val answer: String
)

@Serializable
data class DeleteAnswerInfoModel(
    val questionInfoId: UUID,
    val answerInfoId: UUID
)

@Serializable
data class PostAnswerModel(
    val questionInfoId: UUID,
    val answer: String
)