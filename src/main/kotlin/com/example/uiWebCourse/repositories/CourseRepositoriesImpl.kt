package com.example.uiWebCourse.repositories

import com.example.uiWebCourse.API
import com.example.uiWebCourse.RepositoriesUtils
import com.example.uiWebCourse.models.CheckCourseModel
import com.example.uiWebCourse.models.CourseModel
import com.example.uiWebCourse.models.ResultCourseModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.uuid.UUID
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface CourseRepositories {
    suspend fun getCoursesInfo(): List<CourseModel>

    suspend fun checkCourse(checkCourseModel: CheckCourseModel): Pair<ResultCourseModel?, String?>

    suspend fun deleteCourse(courseId: UUID): Pair<Boolean, String?>
}

class CourseRepositoriesImpl : CourseRepositories, KoinComponent {

    private val client: HttpClient by inject()
    private val repositoriesUtils: RepositoriesUtils by inject()

    override suspend fun getCoursesInfo(): List<CourseModel> {
        return client.get(API.GET_COURSES.url).body()
    }

    override suspend fun checkCourse(checkCourseModel: CheckCourseModel): Pair<ResultCourseModel?, String?> {
        client.post(API.POST_CHECK_COURSE.url) {
            setBody(checkCourseModel)
        }.let { response ->
            return repositoriesUtils.errorHandling(
                boolean = null,
                response = response
            )
        }
    }

    override suspend fun deleteCourse(courseId: UUID): Pair<Boolean, String?> {
        client.delete(API.DELETE_COURSE.url + "/${courseId}").let { response ->
            return Pair(response.status.isSuccess(), response.body())
        }
    }
}