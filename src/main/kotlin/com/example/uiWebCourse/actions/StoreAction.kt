package com.example.uiWebCourse.actions

import com.example.uiWebCourse.models.*
import io.kvision.redux.RAction
import kotlinx.serialization.Serializable

@Serializable
data class StoreState(
    val errorMessage: String? = null,
    val userInfo: GetUserModel? = null,
    val tokens: TokensModel? = null,
    val listCourse: List<CourseModel>? = null,
    val listQuestions: List<QuestionsInfoModel>? = null,
    val nameSelectCourse: String? = null,
    val resultCourse: ResultCourseModel? = null,
    val loading: Boolean = false
)

sealed class StoreAction : RAction {
    data class Error(val errorMessage: String?) : StoreAction()
    data class SetUserInfo(val userInfo: GetUserModel) : StoreAction()
    data class SetTokens(val tokens: TokensModel) : StoreAction()
    data class SetListCourse(val listCourse: List<CourseModel>) : StoreAction()
    data class SetListQuestions(val listQuestions: List<QuestionsInfoModel>) : StoreAction()
    data class SetNameSelectCourse(val nameSelectCourse: String?) : StoreAction()
    data class SetResultCourse(val resultCourse: ResultCourseModel?) : StoreAction()
    data class SetLoading(val loading: Boolean) : StoreAction()
}

fun storeReducer(state: StoreState, action: StoreAction): StoreState = when (action) {
    is StoreAction.Error -> state.copy(errorMessage = action.errorMessage)
    is StoreAction.SetUserInfo -> state.copy(userInfo = action.userInfo)
    is StoreAction.SetTokens -> state.copy(tokens = action.tokens)
    is StoreAction.SetListCourse -> state.copy(listCourse = action.listCourse)
    is StoreAction.SetListQuestions -> state.copy(listQuestions = action.listQuestions)
    is StoreAction.SetNameSelectCourse -> state.copy(nameSelectCourse = action.nameSelectCourse)
    is StoreAction.SetResultCourse -> state.copy(resultCourse = action.resultCourse)
    is StoreAction.SetLoading -> state.copy(loading = action.loading)
}