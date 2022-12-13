package com.example.uiWebCourse.manaders

import com.example.uiWebCourse.AppScope
import com.example.uiWebCourse.RootUi
import com.example.uiWebCourse.actions.StoreAction
import com.example.uiWebCourse.actions.StoreState
import com.example.uiWebCourse.repositories.CourseRepositories
import io.kvision.redux.ActionCreator
import io.kvision.routing.routing
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface CourseManager {
    suspend fun getCourseInfo(): ActionCreator<dynamic, StoreState>
}

class CourseManagerImpl : CourseManager, KoinComponent {
    override suspend fun getCourseInfo(): ActionCreator<dynamic, StoreState> {
        return { dispatch, _ ->
            val courseRep: CourseRepositories by inject()
            AppScope.launch {
                courseRep.getCoursesInfo().let { courses ->
                    dispatch(StoreAction.SetListCourse(courses)).let {
                        routing.navigate(RootUi.HOME.url)
                    }
                }
            }
        }
    }
}