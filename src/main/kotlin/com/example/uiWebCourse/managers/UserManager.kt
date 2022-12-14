package com.example.uiWebCourse.managers

import com.example.uiWebCourse.AppScope
import com.example.uiWebCourse.RootUi
import com.example.uiWebCourse.TokensDataStore
import com.example.uiWebCourse.actions.StoreAction
import com.example.uiWebCourse.actions.StoreState
import com.example.uiWebCourse.models.LoginModel
import com.example.uiWebCourse.models.RegisterUserModel
import com.example.uiWebCourse.repositories.UserRepositories
import io.kvision.redux.ActionCreator
import io.kvision.redux.ReduxStore
import io.kvision.routing.routing
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface UserManager {
    suspend fun registration(regModel: RegisterUserModel): ActionCreator<dynamic, StoreState>

    suspend fun login(
        store: ReduxStore<StoreState, StoreAction>,
        logModel: LoginModel
    ): ActionCreator<dynamic, StoreState>

    suspend fun getUserInfo(): ActionCreator<dynamic, StoreState>
}

class UserManagerImpl : UserManager, KoinComponent {

    private val courseManager: CourseManager by inject()
    override suspend fun registration(
        regModel: RegisterUserModel
    ): ActionCreator<dynamic, StoreState> {
        return { dispatch, _ ->
            val userRep: UserRepositories by inject()
            AppScope.launch {
                userRep.registerUser(regModel).let { (isSuccess, errorMessage) ->
                    if (isSuccess) {
                        routing.navigate(RootUi.LOGIN.url)
                    } else {
                        dispatch(StoreAction.Error(errorMessage = errorMessage ?: "Unknown error"))
                    }
                }
            }
        }
    }

    override suspend fun login(
        store: ReduxStore<StoreState, StoreAction>,
        logModel: LoginModel
    ): ActionCreator<dynamic, StoreState> {
        return { dispatch, _ ->
            val userRep: UserRepositories by inject()
            val tokensDataStore: TokensDataStore by inject()
            AppScope.launch {
                userRep.loginUser(logModel).let { (tokens, errorMessage) ->
                    if (tokens != null) {
                        tokensDataStore.setTokens(tokens).let {
                            launch {
                                store.dispatch(courseManager.getCourseInfo())
                            }
                            launch {
                                store.dispatch(getUserInfo())
                            }
                        }
                    } else {
                        dispatch(StoreAction.Error(errorMessage = errorMessage ?: "Unknown error"))
                    }
                }
            }
        }
    }

    override suspend fun getUserInfo(): ActionCreator<dynamic, StoreState> {
        return { dispatch, _ ->
            val userRep: UserRepositories by inject()
            AppScope.launch {
                userRep.getUserInfo().let { (user, errorMessage) ->
                    if (user != null) {
                        dispatch(StoreAction.SetUserInfo(user)).let {
                            routing.navigate(RootUi.COURSES.url)
                        }
                    } else {
                        dispatch(
                            StoreAction.Error(
                                errorMessage = errorMessage ?: "Unknown error"
                            )
                        ).let {
                            routing.navigate(RootUi.LOGIN.url)
                        }
                    }
                }
            }
        }
    }
}