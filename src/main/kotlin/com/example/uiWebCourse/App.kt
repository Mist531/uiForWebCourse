package com.example.uiWebCourse

import com.example.uiWebCourse.actions.StoreAction
import com.example.uiWebCourse.actions.StoreState
import com.example.uiWebCourse.actions.storeReducer
import com.example.uiWebCourse.manaders.CourseManager
import com.example.uiWebCourse.manaders.UserManager
import com.example.uiWebCourse.modules.clientModule
import com.example.uiWebCourse.modules.commonModule
import com.example.uiWebCourse.modules.managersModule
import com.example.uiWebCourse.modules.repositoriesModule
import com.example.uiWebCourse.ui.CoursesPanel
import com.example.uiWebCourse.ui.LoginPanel
import com.example.uiWebCourse.ui.RegistrationPanel
import io.kvision.*
import io.kvision.core.AlignItems
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.i18n.I18n.tr
import io.kvision.modal.Alert
import io.kvision.panel.VPanel
import io.kvision.panel.root
import io.kvision.panel.stackPanel
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

    private val tokensDataStore: TokensDataStore by inject()
    private val courseManager: CourseManager by inject()
    private val userManager: UserManager by inject()

    init {
        Routing.init(null, true)

        startKoin {
            modules(
                clientModule, repositoriesModule, commonModule, managersModule
            )
        }

        initStore(tokensDataStore = tokensDataStore)
    }

    private val store = createReduxStore(
        ::storeReducer,
        StoreState()
    )


    override fun start() {
        root("kvapp").bind(store) { storeState ->
            stackPanel {
                if (store.getState().errorMessage != null) {
                    Alert.show(
                        caption = tr("Error"),
                        text = store.getState().errorMessage.toString(),
                        animation = true,
                        centered = true
                    ) {
                        store.dispatch(StoreAction.Error(errorMessage = null))
                    }
                }
                add(
                    panel = RegistrationPanel(
                        registrationClick = { regModel ->
                            AppScope.launch {
                                this@App.store.dispatch(
                                    userManager.registration(regModel)
                                )
                            }
                        }
                    ),
                    route = RootUi.REGISTER.url
                )
                add(
                    panel = LoginPanel(
                        loginClick = { loginModel ->
                            AppScope.launch {
                                this@App.store.dispatch(
                                    userManager.login(
                                        store = store,
                                        loginModel
                                    )
                                )
                            }
                        }
                    ),
                    route = RootUi.LOGIN.url
                )
                add(
                    panel = CoursesPanel(
                        goCourseClick = {coursesUUID->
                            AppScope.launch {

                            }
                        },
                        storeState = store.getState(),
                        tokensDataStore = tokensDataStore,
                    ),
                    route = RootUi.COURSES.url
                )
            }
        }
    }

    private fun initStore(
        tokensDataStore: TokensDataStore
    ) {
        if (tokensDataStore.getJwtTokens() == null) {
            routing.navigate(RootUi.LOGIN.url)
        } else {
            AppScope.launch {
                launch {
                    store.dispatch(userManager.getUserInfo())
                }
                launch {
                    store.dispatch(courseManager.getCourseInfo())
                }
            }
            routing.navigate(RootUi.COURSES.url)
        }
    }
}

fun main() {
    startApplication(::App, module.hot, BootstrapModule, BootstrapCssModule, CoreModule)
}

