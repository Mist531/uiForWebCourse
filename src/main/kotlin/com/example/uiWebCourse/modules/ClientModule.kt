package com.example.uiWebCourse.modules

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val clientModule = module {
    factory {
        HttpClient(Js){
            install(Logging)
            install(ContentNegotiation){
                json(getKoin().get())
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