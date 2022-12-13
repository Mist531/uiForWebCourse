package com.example.uiWebCourse

import com.example.uiWebCourse.models.TokensModel
import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.w3c.dom.set

class TokensDataStore {
    private val JWT_TOKEN_ACCESS = "jwtTokenAccess"
    private val JWT_TOKEN_REFRESH = "jwtTokenRefresh"

    private var accessToken: StateFlow<String?> = MutableStateFlow(
        localStorage.getItem(JWT_TOKEN_ACCESS)
    )

    private var refreshToken: StateFlow<String?> = MutableStateFlow(
        localStorage.getItem(JWT_TOKEN_REFRESH)
    )

    fun getJwtTokens(): TokensModel? {
        val accessToken = localStorage.getItem(JWT_TOKEN_ACCESS)
        val refreshToken = localStorage.getItem(JWT_TOKEN_REFRESH)
        return if (accessToken == null || refreshToken == null) {
            null
        }else{
            TokensModel(accessToken, refreshToken)
        }
    }

    fun setTokens(tokensModel: TokensModel) {
        localStorage[JWT_TOKEN_ACCESS] = tokensModel.access
        localStorage[JWT_TOKEN_REFRESH] = tokensModel.refresh
        accessToken = MutableStateFlow(
            localStorage.getItem(JWT_TOKEN_ACCESS)
        )
        refreshToken = MutableStateFlow(
            localStorage.getItem(JWT_TOKEN_REFRESH)
        )
    }
    fun getAccessToken(): StateFlow<String?> = accessToken

    fun clearTokens(){
        localStorage.clear()
    }
}