package com.example.uiWebCourse.modules

import com.example.uiWebCourse.managers.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val managersModule = module {
    singleOf<CourseManager>(::CourseManagerImpl)
    singleOf<UserManager>(::UserManagerImpl)
    singleOf<QuestionManager>(::QuestionManagerImpl)
}