package com.example.uiWebCourse.repositories

import com.example.uiWebCourse.API
import com.example.uiWebCourse.RepositoriesUtils
import com.example.uiWebCourse.models.QuestionsInfoModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.uuid.UUID
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface QuestionRepositories {

    suspend fun getQuestions(courseInfoId: UUID): Pair<List<QuestionsInfoModel>?, String?>

    suspend fun deleteQuestion(questionInfoId: UUID): Pair<Boolean, String?>
}

class QuestionRepositoriesImpl : QuestionRepositories, KoinComponent {

    private val client: HttpClient by inject()
    private val repositoriesUtils: RepositoriesUtils by inject()

    override suspend fun getQuestions(courseInfoId: UUID): Pair<List<QuestionsInfoModel>?, String?> {
        client.get(API.GET_QUESTIONS.url + "/${courseInfoId}").let { response ->
            return repositoriesUtils.errorHandling(
                boolean = null,
                response = response
            )
        }
    }

    override suspend fun deleteQuestion(questionInfoId: UUID): Pair<Boolean, String?> {
        client.delete(API.DELETE_QUESTION.url + "/${questionInfoId}").let { response ->
            return Pair(response.status.isSuccess(), response.body())
        }
    }
}