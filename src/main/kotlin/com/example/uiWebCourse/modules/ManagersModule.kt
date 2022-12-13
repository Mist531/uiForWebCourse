package com.example.uiWebCourse.modules

import com.example.uiWebCourse.manaders.CourseManager
import com.example.uiWebCourse.manaders.CourseManagerImpl
import com.example.uiWebCourse.manaders.UserManager
import com.example.uiWebCourse.manaders.UserManagerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val managersModule = module{
    singleOf<CourseManager>(::CourseManagerImpl)
    singleOf<UserManager>(::UserManagerImpl)
}