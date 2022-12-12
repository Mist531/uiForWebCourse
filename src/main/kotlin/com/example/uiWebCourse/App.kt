package com.example.uiWebCourse

import com.example.uiWebCourse.actions.UserAction
import com.example.uiWebCourse.actions.UserActionModel
import com.example.uiWebCourse.actions.userReducer
import com.example.uiWebCourse.models.LoginModel
import com.example.uiWebCourse.models.RegisterUserModel
import com.example.uiWebCourse.modules.clientModule
import com.example.uiWebCourse.modules.repositoriesModule
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
        Routing.init()
    }

    private val userStore = createReduxStore(
        ::userReducer,
        UserActionModel()
    )

    override fun start() {

        startKoin {
            modules(
                clientModule, repositoriesModule
            )
        }

        /*val testModel = RegisterUserModel(
            login = "test",
            password = "test",
            firstName = "test",
            lastName = "test",
            patronymic = "test",
        )*/

        root("kvapp").bind(userStore) { store ->
            stackPanel {
                add(
                    panel = RegistrationPanel(
                        registration = { regModel ->
                            userStore.dispatch(
                                registration(regModel)
                            )
                        },
                        store = store
                    ),
                    route = RootUi.REGISTER.url
                )
                add(
                    panel = LoginPanel(
                        login = { loginModel ->
                            userStore.dispatch(
                                login(loginModel)
                            )
                        },
                        store = store
                    ),
                    route = RootUi.LOGIN.url
                )
                add(
                    panel = VPanel() {
                        div {
                            +"Hello, ${store.user?.firstName} ${store.user?.lastName} ${store.user?.patronymic}"
                        }
                    },
                    route = RootUi.HOME.url
                )
            }
        }
    }

    private fun registration(
        regModel: RegisterUserModel
    ): ActionCreator<dynamic, UserActionModel> {
        return { dispatch, _ ->
            val userRep: UserRepositories by inject()
            AppScope.launch {
                userRep.registerUser(regModel).let { (isSuccess, errorMessage) ->
                    if (isSuccess) {
                        dispatch(UserAction.SetRegistration(isSuccess)).let {
                            routing.kvNavigate(RootUi.LOGIN.url)
                        }
                    } else {
                        dispatch(UserAction.SetRegistration(isSuccess))
                        dispatch(UserAction.Error(errorMessage = errorMessage ?: "Unknown error"))
                    }
                }
            }
        }
    }

    private fun login(
        logModel: LoginModel
    ): ActionCreator<dynamic, UserActionModel> {
        return { dispatch, _ ->
            val userRep: UserRepositories by inject()
            AppScope.launch {
                userRep.loginUser(logModel).let { (tokens, errorMessage) ->
                    if (tokens != null) {
                        dispatch(UserAction.SetTokens(tokens)).let {
                            userRep.getUserInfo(tokens).let { (user, errorMessage) ->
                                console.log("USER: " + user.toString())
                                if (user != null) {
                                    dispatch(UserAction.SetUser(user)).let {
                                        routing.kvNavigate(RootUi.HOME.url)
                                    }
                                } else {
                                    dispatch(UserAction.Error(errorMessage = errorMessage ?: "Unknown error")).let {
                                        routing.kvNavigate(RootUi.LOGIN.url)
                                    }
                                }
                            }
                        }
                    } else {
                        dispatch(UserAction.Error(errorMessage = errorMessage ?: "Unknown error"))
                    }
                }
            }
        }
    }

}

fun main() {
    startApplication(::App, module.hot, BootstrapModule, BootstrapCssModule, CoreModule)
}

