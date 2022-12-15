package com.example.uiWebCourse.repositories

import com.example.uiWebCourse.API
import com.example.uiWebCourse.RepositoriesUtils
import com.example.uiWebCourse.models.GetUserModel
import com.example.uiWebCourse.models.LoginModel
import com.example.uiWebCourse.models.RegisterUserModel
import com.example.uiWebCourse.models.TokensModel
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface UserRepositories {

    suspend fun registerUser(regModel: RegisterUserModel): Pair<Boolean, String?>

    suspend fun loginUser(logModel: LoginModel): Pair<TokensModel?, String?>

    suspend fun getUserInfo(): Pair<GetUserModel?, String?>

    suspend fun refreshTokens(): Pair<TokensModel?, String?>
}

class UserRepositoriesImpl : UserRepositories, KoinComponent {

    private val client: HttpClient by inject()
    private val repositoriesUtils: RepositoriesUtils by inject()

    override suspend fun registerUser(regModel: RegisterUserModel): Pair<Boolean, String?> {
        client.post(API.POST_REGISTER.url) {
            setBody(regModel)
        }.let { response ->
            return repositoriesUtils.errorHandling(
                boolean = response.status.isSuccess(),
                response = response
            )
        }

    }

    override suspend fun loginUser(logModel: LoginModel): Pair<TokensModel?, String?> {
        client.post(API.POST_LOGIN.url) {
            setBody(logModel)
        }.let { response ->
            return repositoriesUtils.errorHandling(
                boolean = null,
                response = response
            )
        }
    }

    override suspend fun getUserInfo(): Pair<GetUserModel?, String?> {
        client.get(API.GET_USER_INFO.url).let { response ->
            return repositoriesUtils.errorHandling(
                boolean = null,
                response = response
            )
        }
    }

    override suspend fun refreshTokens(): Pair<TokensModel?, String?> {
        client.get(API.GET_UPDATE_TOKENS.url).let { response ->
            return repositoriesUtils.errorHandling(
                boolean = null,
                response = response
            )
        }
    }
}