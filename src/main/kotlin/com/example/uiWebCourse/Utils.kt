package com.example.uiWebCourse

fun String.isEmailValid(): Boolean =
    this.isNotEmpty() && this.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$"))