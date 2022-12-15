package com.example.uiWebCourse

fun String.isEmailValid(): Boolean =
    this.isNotEmpty() && this.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$"))

enum class AdminInfo(val value: String) {
    DELETE_COURSE("Курс удалён, перезагрузите страницу."),
    DELETE_QUESTION("Вопрос удалён, перезагрузите страницу."),
}