package com.example.uiWebCourse.actions

import com.example.uiWebCourse.models.CourseModel
import com.example.uiWebCourse.models.GetUserModel
import com.example.uiWebCourse.models.TokensModel
import io.kvision.redux.RAction
import kotlinx.serialization.Serializable

@Serializable
data class StoreState(
    val errorMessage: String? = null,
    val userInfo: GetUserModel? = null,
    val tokens: TokensModel? = null,
    val listCourse: List<CourseModel>? = null,
)

sealed class StoreAction : RAction {
    data class Error(val errorMessage: String) : StoreAction()
    data class SetUserInfo(val userInfo: GetUserModel) : StoreAction()
    data class SetTokens(val tokens: TokensModel) : StoreAction()
    data class SetListCourse(val listCourse: List<CourseModel>) : StoreAction()
}

fun storeReducer(state: StoreState, action: StoreAction): StoreState = when (action) {
    is StoreAction.Error -> state.copy(errorMessage = action.errorMessage)
    is StoreAction.SetUserInfo -> state.copy(userInfo = action.userInfo)
    is StoreAction.SetTokens -> state.copy(tokens = action.tokens)
    is StoreAction.SetListCourse -> state.copy(listCourse = action.listCourse)
}