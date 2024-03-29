package com.example.uiWebCourse.models

import kotlinx.serialization.Serializable
import kotlinx.uuid.UUID

@Serializable
data class UUIDCourse(
    val courseInfoId: UUID
)

@Serializable
data class CourseModel(
    val courseInfoId: UUID,
    val name: String,
    val description: String? = null
)

@Serializable
data class CheckQuestionModel(
    val questionsInfoId: UUID,
    val selectAnswerId: UUID
)

@Serializable
data class CheckQuestionModelString(
    val selectAnswerId: String
)

@Serializable
data class CheckCourseModel(
    val courseInfoId: UUID,
    val questions: List<CheckQuestionModel>
)

@Serializable
data class ResultCourseModel(
    val rightAnswer: Int,
    val wrongAnswer: Int,
    val listWrongQuestion: List<String>
)