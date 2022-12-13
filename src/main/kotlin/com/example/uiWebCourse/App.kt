package com.example.uiWebCourse

import com.example.uiWebCourse.actions.StoreAction
import com.example.uiWebCourse.actions.StoreState
import com.example.uiWebCourse.actions.storeReducer
import com.example.uiWebCourse.models.LoginModel
import com.example.uiWebCourse.models.RegisterUserModel
import com.example.uiWebCourse.modules.clientModule
import com.example.uiWebCourse.modules.commonModule
import com.example.uiWebCourse.modules.repositoriesModule
import com.example.uiWebCourse.repositories.CourseRepositories
import com.example.uiWebCourse.repositories.UserRepositories
import com.example.uiWebCourse.ui.LoginPanel
import com.example.uiWebCourse.ui.RegistrationPanel
import io.kvision.*
import io.kvision.html.div
import io.kvision.panel.VPanel
import io.kvision.panel.root
import io.kvision.panel.stackPanel
import io.kvision.redux.ActionCreator
import io.kvision.redux.createReduxStore
import io.kvision.routing.Routing
import io.kvision.routing.routing
import io.kvision.state.bind
import io.kvision.utils.px
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

val AppScope = CoroutineScope(window.asCoroutineDispatcher())

class App : Application(), KoinComponent {

    init {
        Routing.init(null, true)
    }

    private val store = createReduxStore(
        ::storeReducer,
        StoreState()
    )

    override fun start() {
        startKoin {
            modules(
                clientModule, repositoriesModule, commonModule
            )
        }

        val tokensDataStore: TokensDataStore by inject()

        if(tokensDataStore.getJwtTokens() == null){
            routing.navigate(RootUi.LOGIN.url)
        }else{
            AppScope.launch {
                launch {
                    store.dispatch(getUserInfo())
                }
                launch {
                    store.dispatch(getCourseInfo())
                }
            }
            routing.navigate(RootUi.HOME.url)
        }

        root("kvapp").bind(store) { store ->
            stackPanel {
                add(
                    panel = RegistrationPanel(
                        registrationClick = { regModel ->
                            this@App.store.dispatch(
                                registration(regModel)
                            )
                        }
                    ),
                    route = RootUi.REGISTER.url
                )
                add(
                    panel = LoginPanel(
                        loginClick = { loginModel ->
                            this@App.store.dispatch(
                                login(loginModel)
                            )
                        }
                    ),
                    route = RootUi.LOGIN.url
                )
                add(
                    panel = VPanel {
                        div {
                            this.marginBottom = 50.px
                            +"Hello, ${store.userInfo?.firstName} ${store.userInfo?.lastName} ${store.userInfo?.patronymic}"
                        }
                        store.listCourse?.forEach {
                            div {
                                +it.name
                            }
                            div {
                                +"${it.description}"
                            }
                        }
                    },
                    route = RootUi.HOME.url
                )
            }
        }
    }

    private fun registration(
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

    private fun login(
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
                                store.dispatch(getCourseInfo())
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

    private fun getCourseInfo(): ActionCreator<dynamic, StoreState> {
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

    private fun getUserInfo(): ActionCreator<dynamic, StoreState> {
        return { dispatch, _ ->
            val userRep: UserRepositories by inject()
            AppScope.launch {
                userRep.getUserInfo().let{(user, errorMessage) ->
                    if (user != null) {
                        dispatch(StoreAction.SetUserInfo(user)).let {
                            routing.navigate(RootUi.HOME.url)
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

fun main() {
    startApplication(::App, module.hot, BootstrapModule, BootstrapCssModule, CoreModule)
}

