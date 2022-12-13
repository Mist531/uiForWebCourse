package com.example.uiWebCourse.modules

import com.example.uiWebCourse.TokensDataStore
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val clientModule = module {
    factory {
        val tokensDataStore: TokensDataStore by inject()
        HttpClient(Js){
            install(Logging)
            install(ContentNegotiation){
                json(getKoin().get())
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                tokensDataStore.getAccessToken().value.let {
                    if (it != null) {
                        header("Authorization", "${AuthScheme.Bearer} $it")
                    }
                }
            }
        }
    }
    single {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
}