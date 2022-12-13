package com.example.uiWebCourse.repositories

import com.example.uiWebCourse.API
import com.example.uiWebCourse.models.CourseModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface CourseRepositories {
    suspend fun getCoursesInfo(): List<CourseModel>
}

class CourseRepositoriesImpl : CourseRepositories, KoinComponent {

    private val client: HttpClient by inject()
    override suspend fun getCoursesInfo(): List<CourseModel> {
        return client.get(API.GET_COURSES.url).body<List<CourseModel>>()
    }
}