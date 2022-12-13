package com.example.uiWebCourse

import com.example.uiWebCourse.actions.StoreState
import com.example.uiWebCourse.actions.storeReducer
import com.example.uiWebCourse.manaders.CourseManager
import com.example.uiWebCourse.manaders.UserManager
import com.example.uiWebCourse.modules.clientModule
import com.example.uiWebCourse.modules.commonModule
import com.example.uiWebCourse.modules.managersModule
import com.example.uiWebCourse.modules.repositoriesModule
import com.example.uiWebCourse.ui.LoginPanel
import com.example.uiWebCourse.ui.RegistrationPanel
import io.kvision.*
import io.kvision.core.AlignItems
import io.kvision.html.button
import io.kvision.html.div
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
                    panel = VPanel {
                        button("Logout") {
                            width = 200.px
                            alignItems = AlignItems.CENTER
                            onClick {
                                AppScope.launch {
                                    tokensDataStore.clearTokens().let {
                                        routing.navigate(RootUi.LOGIN.url)
                                    }
                                }
                            }
                        }
                        div {
                            this.marginBottom = 50.px
                            +"Hello, ${storeState.userInfo?.firstName} ${storeState.userInfo?.lastName} ${storeState.userInfo?.patronymic}"
                        }
                        storeState.listCourse?.forEach {
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
            routing.navigate(RootUi.HOME.url)
        }
    }
}

fun main() {
    startApplication(::App, module.hot, BootstrapModule, BootstrapCssModule, CoreModule)
}

