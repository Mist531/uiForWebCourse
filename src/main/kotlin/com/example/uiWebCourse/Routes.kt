package com.example.uiWebCourse

enum class DefaultUrl(val url: String) {
    DEFAULT("http://localhost:8081/api"),
}

enum class RootUi(val url: String) {
    LOGIN("/login"),
    REGISTER("/register"),
    QUESTIONS("/questions"),
    COURSES("/courses"),
}

enum class API(val url: String) {
    POST_LOGIN(DefaultUrl.DEFAULT.url + "/login"),
    POST_REGISTER(DefaultUrl.DEFAULT.url + "/register"),
    GET_USER_INFO(DefaultUrl.DEFAULT.url + "/user/info"),
    GET_UPDATE_TOKENS(DefaultUrl.DEFAULT.url + "/user/refresh"),
    GET_COURSES(DefaultUrl.DEFAULT.url + "/course"),
    POST_CHECK_COURSE(DefaultUrl.DEFAULT.url + "/course/check"),
    PUT_COURSE(DefaultUrl.DEFAULT.url + "/course"),
    POST_COURSE(DefaultUrl.DEFAULT.url + "/course"),
    DELETE_COURSE(DefaultUrl.DEFAULT.url + "/course"),
    GET_QUESTIONS(DefaultUrl.DEFAULT.url + "/getQuestion"),
    POST_QUESTION(DefaultUrl.DEFAULT.url + "/question"),
    PUT_QUESTION(DefaultUrl.DEFAULT.url + "/question"),
    DELETE_QUESTION(DefaultUrl.DEFAULT.url + "/question"),
}