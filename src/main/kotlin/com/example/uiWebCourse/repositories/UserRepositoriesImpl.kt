package com.example.uiWebCourse.repositories

import com.example.uiWebCourse.API
import com.example.uiWebCourse.models.GetUserModel
import com.example.uiWebCourse.models.LoginModel
import com.example.uiWebCourse.models.RegisterUserModel
import com.example.uiWebCourse.models.TokensModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.auth.AuthScheme.Bearer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface UserRepositories {

    suspend fun registerUser(regModel: RegisterUserModel): Pair<Boolean, String?>

    suspend fun loginUser(logModel: LoginModel): Pair<TokensModel?, String?>

    suspend fun getUserInfo(tokens: TokensModel): Pair<GetUserModel?, String?>
}

class UserRepositoriesImpl : UserRepositories, KoinComponent {

    private val client: HttpClient by inject()

    override suspend fun registerUser(regModel: RegisterUserModel): Pair<Boolean, String?> {
        val response = client.post(API.POST_REGISTER.url) {
            contentType(ContentType.Application.Json)
            setBody(regModel)
        }
        return Pair(response.status.isSuccess(), response.body())
    }

    override suspend fun loginUser(logModel: LoginModel): Pair<TokensModel?, String?> {
        client.post(API.POST_LOGIN.url) {
            contentType(ContentType.Application.Json)
            setBody(logModel)
        }.let {
            return if (it.status.isSuccess()){
                console.log("TOKENS: " + it.body<TokensModel>().toString())
                Pair(it.body<TokensModel>(), null)
            } else {
                Pair(null, it.body<String>())
            }
        }
    }

    override suspend fun getUserInfo(tokens: TokensModel): Pair<GetUserModel?, String?> {
        client.get(API.GET_USER_INFO.url) {
            contentType(ContentType.Application.Json)
            header("Authorization", Bearer + " " + tokens.access)
        }.let {
            return if (it.status.isSuccess()){
                console.log("USER: " + it.body<GetUserModel?>().toString())
                Pair(it.body<GetUserModel>(), null)
            } else {
                Pair(null, it.body<String>())
            }
        }
    }

}