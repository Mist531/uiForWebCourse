package com.example.uiWebCourse

import com.example.uiWebCourse.actions.SelectCourse
import com.example.uiWebCourse.actions.StoreAction
import com.example.uiWebCourse.actions.StoreState
import com.example.uiWebCourse.actions.storeReducer
import com.example.uiWebCourse.managers.CourseManager
import com.example.uiWebCourse.managers.QuestionManager
import com.example.uiWebCourse.managers.UserManager
import com.example.uiWebCourse.models.ResultCourseModel
import com.example.uiWebCourse.modules.clientModule
import com.example.uiWebCourse.modules.commonModule
import com.example.uiWebCourse.modules.managersModule
import com.example.uiWebCourse.modules.repositoriesModule
import com.example.uiWebCourse.ui.CoursesPanel
import com.example.uiWebCourse.ui.LoginPanel
import com.example.uiWebCourse.ui.QuestionsPanel
import com.example.uiWebCourse.ui.RegistrationPanel
import io.kvision.*
import io.kvision.i18n.I18n.tr
import io.kvision.modal.Alert
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
import kotlinx.uuid.UUID
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

val AppScope = CoroutineScope(window.asCoroutineDispatcher())

class App : Application(), KoinComponent {

    private val tokensDataStore: TokensDataStore by inject()
    private val courseManager: CourseManager by inject()
    private val userManager: UserManager by inject()
    private val questionManager: QuestionManager by inject()

    init {
        Routing.init(null, true, Strategy.ALL)

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
                if (storeState.errorMessage != null) {
                    Alert.show(
                        caption = tr("Error"),
                        text = storeState.errorMessage.toString(),
                        centered = true
                    ) {
                        store.dispatch(StoreAction.Error(errorMessage = null))
                    }
                }
                if (storeState.resultCourse != null) {
                    Alert.show(
                        caption = tr("Результат"),
                        text = textResultCheckCourse(storeState.resultCourse),
                        centered = true
                    ) {
                        store.dispatch(StoreAction.SetResultCourse(resultCourse = null))
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
                        goCourseClick = { coursesUUID ->
                            AppScope.launch {
                                launch {
                                    this@App.store.dispatch(
                                        StoreAction.SetNameSelectCourse(
                                            SelectCourse(
                                                courseInfoId = coursesUUID,
                                                courseName = store.getState().listCourse?.find {
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
                                this@App.store.dispatch(
                                    courseManager.checkCourseAnswer(
                                        checkCourseModel
                                    )
                                )
                            }
                        },
                        storeState = store.getState(),
                    ),
                    route = RootUi.QUESTIONS.url
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

    private fun textResultCheckCourse(
        resultCourseModel: ResultCourseModel
    ): String {
        return if (resultCourseModel.wrongAnswer <= 0) {
            "Поздравляем! Вы прошли курс!"
        } else {
            val listWrongQuestion = resultCourseModel.listWrongQuestion
            "Вы ответили неправильно на ${resultCourseModel.wrongAnswer} вопросов.\n\n" +
                    "Список неправильных ответов: ${
                        resultCourseModel.listWrongQuestion.mapIndexed { index, wrongQuestion ->
                            if (index != listWrongQuestion.size - 1) {
                                "${getNameQuestion(wrongQuestion = wrongQuestion)}, "
                            } else {
                                "${getNameQuestion(wrongQuestion = wrongQuestion)}."
                            }
                        }.joinToString(separator = "")
                    }"
        }
    }

    private fun getNameQuestion(
        wrongQuestion: String,
        storeState: StoreState = store.getState(),
    ): String {
        return storeState.listQuestions?.let {
            it.find {
                it.questionInfoId == UUID(wrongQuestion)
            }
        }?.question ?: "ID Вопроса: $wrongQuestion"
    }
}

fun main() {
    startApplication(::App, module.hot, BootstrapModule, BootstrapCssModule, CoreModule)
}

