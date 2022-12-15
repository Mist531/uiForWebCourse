package com.example.uiWebCourse

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.koin.core.component.KoinComponent

class RepositoriesUtils : KoinComponent {
    suspend inline fun <reified T> errorHandling(
        boolean: T,
        response: HttpResponse,
    ): Pair<T, String?>{
        return if (response.status.isSuccess()) {
            Pair(response.body<T>(), null)
        } else {
            val error = response.body<String>()
            return if(error.isEmpty() || error.isBlank()) {
                Pair(boolean, response.status.toString())
            }else{
                Pair(boolean, error)
            }
        }
    }
}