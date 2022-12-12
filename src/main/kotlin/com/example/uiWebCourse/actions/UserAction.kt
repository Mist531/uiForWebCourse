package com.example.uiWebCourse.actions

import com.example.uiWebCourse.models.GetUserModel
import com.example.uiWebCourse.models.TokensModel
import io.kvision.redux.RAction
import kotlinx.serialization.Serializable
import kotlinx.uuid.UUID

@Serializable
data class UserActionModel(
    val errorMessage: String? = null,
    val registrationOk: Boolean? = null,
    val user: GetUserModel? = null,
    val tokens: TokensModel? = null,
)

sealed class UserAction : RAction {
    object Register : UserAction()
    object LogIn : UserAction()
    data class Error(val errorMessage: String) : UserAction()
    data class SetUser(val user: GetUserModel) : UserAction()
    data class SetRegistration(val boolean: Boolean) : UserAction()
    data class SetTokens(val tokens: TokensModel) : UserAction()
}

fun userReducer(state: UserActionModel, action: UserAction): UserActionModel = when (action) {
    is UserAction.Error -> state.copy(errorMessage = action.errorMessage)
    is UserAction.Register -> state.copy(errorMessage = null)
    is UserAction.LogIn -> state.copy(errorMessage = null)
    is UserAction.SetUser -> state.copy(user = action.user)
    is UserAction.SetRegistration -> state.copy(registrationOk = action.boolean)
    is UserAction.SetTokens -> state.copy(tokens = action.tokens)
}