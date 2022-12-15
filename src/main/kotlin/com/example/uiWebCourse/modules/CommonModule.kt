package com.example.uiWebCourse.modules

import com.example.uiWebCourse.TokensDataStore
import com.example.uiWebCourse.UiUtils
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonModule = module {
    singleOf(::TokensDataStore)
    singleOf(::UiUtils)
}