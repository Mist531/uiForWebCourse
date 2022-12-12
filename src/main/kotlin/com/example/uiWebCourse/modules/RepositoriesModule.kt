package com.example.uiWebCourse.modules

import com.example.uiWebCourse.repositories.UserRepositories
import com.example.uiWebCourse.repositories.UserRepositoriesImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoriesModule = module {
    singleOf<UserRepositories>(::UserRepositoriesImpl)
}