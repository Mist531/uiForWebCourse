package com.example.uiWebCourse.repositories

import com.example.uiWebCourse.API
import com.example.uiWebCourse.models.CheckCourseModel
import com.example.uiWebCourse.models.CourseModel
import com.example.uiWebCourse.models.ResultCourseModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface CourseRepositories {

    suspend fun getCoursesInfo(): List<CourseModel>

    suspend fun checkCourse(checkCourseModel: CheckCourseModel): Pair<ResultCourseModel?, String?>
}

class CourseRepositoriesImpl : CourseRepositories, KoinComponent {
    private val client: HttpClient by inject()

    override suspend fun getCoursesInfo(): List<CourseModel> {
        return client.get(API.GET_COURSES.url).body<List<CourseModel>>()
    }

    override suspend fun checkCourse(checkCourseModel: CheckCourseModel): Pair<ResultCourseModel?, String?> {
        return client.post(API.POST_CHECK_COURSE.url) {
            setBody(checkCourseModel)
        }.let {
            if (it.status.isSuccess()) {
                Pair(it.body<ResultCourseModel>(), null)
            } else {
                Pair(null, it.body<String>())
            }
        }
    }
}