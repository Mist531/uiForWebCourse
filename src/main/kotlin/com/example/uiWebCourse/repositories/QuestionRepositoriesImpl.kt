package com.example.uiWebCourse.repositories

import com.example.uiWebCourse.API
import com.example.uiWebCourse.models.QuestionsInfoModel
import com.example.uiWebCourse.models.UUIDCourse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.uuid.UUID
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface QuestionRepositories {

    suspend fun getQuestions(courseInfoId: UUID): Pair<List<QuestionsInfoModel>?, String?>
}

class QuestionRepositoriesImpl : QuestionRepositories, KoinComponent {
    private val client: HttpClient by inject()

    override suspend fun getQuestions(courseInfoId: UUID): Pair<List<QuestionsInfoModel>?, String?> {
        return client.post(API.GET_QUESTIONS.url) {
            setBody(
                UUIDCourse(courseInfoId)
            )
        }.let {
            if (it.status.isSuccess()) {
                Pair(it.body<List<QuestionsInfoModel>>(), null)
            } else {
                Pair(null, it.body<String>())
            }
        }
    }
}