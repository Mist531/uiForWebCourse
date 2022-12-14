package com.example.uiWebCourse.managers

import com.example.uiWebCourse.AppScope
import com.example.uiWebCourse.RootUi
import com.example.uiWebCourse.actions.StoreAction
import com.example.uiWebCourse.actions.StoreState
import com.example.uiWebCourse.repositories.QuestionRepositories
import io.kvision.redux.ActionCreator
import io.kvision.routing.routing
import kotlinx.coroutines.launch
import kotlinx.uuid.UUID
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface QuestionManager {
    suspend fun getQuestions(courseInfoId: UUID): ActionCreator<dynamic, StoreState>
}

class QuestionManagerImpl : QuestionManager, KoinComponent {
    override suspend fun getQuestions(courseInfoId: UUID): ActionCreator<dynamic, StoreState> {
        return { dispatch, _ ->
            val questRep: QuestionRepositories by inject()
            AppScope.launch {
                questRep.getQuestions(courseInfoId).let { (list, errorMessage) ->
                    console.log(list)
                    if (list != null) {
                        dispatch(StoreAction.SetListQuestions(list)).let {
                            routing.navigate(RootUi.QUESTIONS.url)
                        }
                    } else {
                        dispatch(StoreAction.Error(errorMessage)).let {
                            routing.navigate(RootUi.COURSES.url)
                        }
                    }
                }
            }
        }
    }
}