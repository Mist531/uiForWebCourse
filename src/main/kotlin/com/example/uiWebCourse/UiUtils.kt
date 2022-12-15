package com.example.uiWebCourse

import com.example.uiWebCourse.actions.StoreAction
import com.example.uiWebCourse.actions.StoreState
import com.example.uiWebCourse.managers.CourseManager
import com.example.uiWebCourse.managers.UserManager
import com.example.uiWebCourse.models.ResultCourseModel
import io.kvision.core.BsColor
import io.kvision.i18n.I18n
import io.kvision.modal.Alert
import io.kvision.redux.ReduxStore
import io.kvision.routing.routing
import io.kvision.toast.ToastContainer
import io.kvision.toast.ToastContainerPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.uuid.UUID
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UiUtils : KoinComponent {

    private val userManager: UserManager by inject()
    private val courseManager: CourseManager by inject()
    private val tokensDataStore: TokensDataStore by inject()

    suspend fun startLoading(
        store: ReduxStore<StoreState, StoreAction>
    ) {
        store.dispatch(StoreAction.SetLoading(loading = true))
        delay(100)
        store.dispatch(StoreAction.SetLoading(loading = false))
    }

    fun initStore(
        store: ReduxStore<StoreState, StoreAction>
    ) {
        if (tokensDataStore.getJwtTokens() == null) {
            routing.navigate(RootUi.LOGIN.url)
        } else {
            AppScope.launch {
                startLoading(store)
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

    private fun getNameQuestion(
        wrongQuestion: String,
        storeState: StoreState
    ): String {
        return storeState.listQuestions?.let { list ->
            list.find { model ->
                model.questionInfoId == UUID(wrongQuestion)
            }
        }?.question ?: "ID Вопроса: $wrongQuestion"
    }

    fun getUserInfo(
        storeState: StoreState
    ): String {
        var user = ""
        if (storeState.userInfo != null) {
            user += "${storeState.userInfo.lastName} ${storeState.userInfo.firstName}"
            if (storeState.userInfo.patronymic != null) {
                user += " ${storeState.userInfo.patronymic}"
            }
        }
        return user
    }

    private fun textResultCheckCourse(
        resultCourseModel: ResultCourseModel,
        storeState: StoreState
    ): String {
        return if (resultCourseModel.wrongAnswer <= 0) {
            "Поздравляем! Вы прошли курс!"
        } else {
            val listWrongQuestion = resultCourseModel.listWrongQuestion
            "Вы ответили неправильно на ${resultCourseModel.wrongAnswer} ${
                when (resultCourseModel.wrongAnswer) {
                    1 -> "вопрос"
                    2, 3, 4 -> "вопроса"
                    else -> "вопросов"
                }
            }.\n\n" +
                    "Список неправильных ответов: ${
                        resultCourseModel.listWrongQuestion.mapIndexed { index, wrongQuestion ->
                            if (index != listWrongQuestion.size - 1) {
                                "${
                                    getNameQuestion(
                                        wrongQuestion = wrongQuestion,
                                        storeState = storeState
                                    )
                                }, "
                            } else {
                                "${
                                    getNameQuestion(
                                        wrongQuestion = wrongQuestion,
                                        storeState = storeState
                                    )
                                }."
                            }
                        }.joinToString(separator = "")
                    }"
        }
    }

    fun uiDialogComponets(
        storeState: StoreState,
        store: ReduxStore<StoreState, StoreAction>,
        scope: CoroutineScope
    ) {
        val tokensDataStore: TokensDataStore by inject()
        if (storeState.loading) {
            ToastContainer(
                toastContainerPosition = ToastContainerPosition.BOTTOMRIGHT,
            ).showToast(
                message = "Loading...",
                color = BsColor.INFOBG,
                delay = 1500,
                autohide = true
            )
        }
        if (storeState.errorMessage != null) {
            Alert.show(
                caption = I18n.tr("Error"),
                text = storeState.errorMessage.toString(),
                centered = true
            ) {
                if (storeState.errorMessage == "401 Unauthorized" && tokensDataStore.getJwtTokens() != null) {
                    tokensDataStore.clearTokens().let {
                        scope.launch {
                            userManager.refreshTokens(
                                store = store
                            )
                        }
                    }
                }
                store.dispatch(StoreAction.Error(errorMessage = null))
            }
        }
        if (storeState.resultCourse != null) {
            Alert.show(
                caption = I18n.tr("Результат"),
                text = textResultCheckCourse(
                    resultCourseModel = storeState.resultCourse,
                    storeState = storeState
                ),
                centered = true
            ) {
                store.dispatch(StoreAction.SetResultCourse(resultCourse = null))
            }
        }
        if (
            storeState.adminInfo != null &&
            storeState.userInfo?.userId == UUID("404c889c-c9ef-457d-880a-9f649a2768fd")
        ) {
            Alert.show(
                caption = I18n.tr("Администратор"),
                text = storeState.adminInfo.toString(),
                centered = true
            ) {
                store.dispatch(StoreAction.SetAdminInfo(adminInfo = null))
            }
        }
    }
}