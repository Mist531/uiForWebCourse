package com.example.uiWebCourse

import com.example.uiWebCourse.actions.SelectCourse
import com.example.uiWebCourse.actions.StoreAction
import com.example.uiWebCourse.actions.StoreState
import com.example.uiWebCourse.actions.storeReducer
import com.example.uiWebCourse.managers.CourseManager
import com.example.uiWebCourse.managers.QuestionManager
import com.example.uiWebCourse.managers.UserManager
import com.example.uiWebCourse.modules.clientModule
import com.example.uiWebCourse.modules.commonModule
import com.example.uiWebCourse.modules.managersModule
import com.example.uiWebCourse.modules.repositoriesModule
import com.example.uiWebCourse.ui.CoursesPanel
import com.example.uiWebCourse.ui.LoginPanel
import com.example.uiWebCourse.ui.QuestionsPanel
import com.example.uiWebCourse.ui.RegistrationPanel
import io.kvision.*
import io.kvision.panel.root
import io.kvision.panel.stackPanel
import io.kvision.redux.createReduxStore
import io.kvision.routing.Routing
import io.kvision.routing.Strategy
import io.kvision.routing.routing
import io.kvision.state.bind
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

val AppScope = CoroutineScope(window.asCoroutineDispatcher())

class App : Application(), KoinComponent {

    private val tokensDataStore: TokensDataStore by inject()
    private val courseManager: CourseManager by inject()
    private val userManager: UserManager by inject()
    private val questionManager: QuestionManager by inject()
    private val utils: UiUtils by inject()

    private val store = createReduxStore(
        ::storeReducer,
        StoreState()
    )

    init {
        Routing.init(null, true, Strategy.ALL)

        startKoin {
            printLogger(Level.DEBUG)
            modules(
                clientModule, repositoriesModule, commonModule, managersModule
            )
        }

        utils.initStore(store = store)
    }

    override fun start() {
        root("kvapp").bind(store) { storeState ->
            stackPanel {
                utils.uiDialogComponets(
                    storeState = storeState,
                    store = store,
                    scope = AppScope
                )
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
                                utils.startLoading(store = store)
                                store.dispatch(
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
                        goCourseClick = { coursesUUID ->
                            AppScope.launch {
                                utils.startLoading(store = store)
                                launch {
                                    this@App.store.dispatch(
                                        StoreAction.SetNameSelectCourse(
                                            SelectCourse(
                                                courseInfoId = coursesUUID,
                                                courseName = storeState.listCourse?.find {
                                                    it.courseInfoId == coursesUUID
                                                }?.name ?: "ID Курса: $coursesUUID"
                                            )
                                        )
                                    )
                                }
                                launch {
                                    this@App.store.dispatch(
                                        questionManager.getQuestions(
                                            coursesUUID
                                        )
                                    )
                                }
                            }
                        },
                        storeState = store.getState(),
                        tokensDataStore = tokensDataStore,
                        utils = utils,
                        deleteCourse = { courseInfoId ->
                            AppScope.launch {
                                utils.startLoading(store = store)
                                launch {
                                    store.dispatch(
                                        courseManager.deleteCourse(courseInfoId)
                                    )
                                }
                                launch {
                                    store.dispatch(
                                        courseManager.getCourseInfo()
                                    )
                                }
                            }
                        }
                    ),
                    route = RootUi.COURSES.url
                )
                add(
                    panel = QuestionsPanel(
                        backClick = {
                            routing.navigate(RootUi.COURSES.url)
                        },
                        checkCourse = { checkCourseModel ->
                            AppScope.launch {
                                utils.startLoading(store = store)
                                this@App.store.dispatch(
                                    courseManager.checkCourseAnswer(
                                        checkCourseModel
                                    )
                                )
                            }
                        },
                        storeState = storeState,
                        deleteQuestion = { id ->
                            AppScope.launch {
                                utils.startLoading(store = store)
                                launch {
                                    store.dispatch(
                                        questionManager.deleteQuestion(id)
                                    )
                                }
                                launch {
                                    storeState.selectCourse?.courseInfoId?.let { id ->
                                        store.dispatch(
                                            questionManager.getQuestions(
                                                id
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    ),
                    route = RootUi.QUESTIONS.url
                )
            }
        }
    }
}

fun main() {
    startApplication(
        ::App,
        module.hot,
        BootstrapModule,
        BootstrapCssModule,
        CoreModule,
    )
}

