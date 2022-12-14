package com.example.uiWebCourse.models.modelForAdmin

import kotlinx.serialization.Serializable
import kotlinx.uuid.UUID

@Serializable
data class AddQuestionsInfoModel(
    val question: String,
    val courseInfoId: UUID,
    val rightIndex: Int,
    val listAnswer: List<AddAnswerModel>
)

@Serializable
data class AddAnswerModel(
    val index: Int,
    val answer: String
)

@Serializable
data class PutQuestionsInfoModel(
    val questionInfoId: UUID,
    val question: String,
    val courseInfoId: UUID,
    val rightAnswerId: UUID,
    val listAnswer: List<PutAnswersModels>
)

@Serializable
data class PutAnswersModels(
    val answerId: UUID,
    val answer: String
)