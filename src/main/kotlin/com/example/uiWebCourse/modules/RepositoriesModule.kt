package com.example.uiWebCourse.modules

import com.example.uiWebCourse.repositories.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoriesModule = module {
    singleOf<UserRepositories>(::UserRepositoriesImpl)
    singleOf<CourseRepositories>(::CourseRepositoriesImpl)
    singleOf<QuestionRepositories>(::QuestionRepositoriesImpl)
}