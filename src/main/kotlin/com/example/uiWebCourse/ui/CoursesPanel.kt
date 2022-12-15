package com.example.uiWebCourse.ui

import com.example.uiWebCourse.AppScope
import com.example.uiWebCourse.RootUi
import com.example.uiWebCourse.TokensDataStore
import com.example.uiWebCourse.UiUtils
import com.example.uiWebCourse.actions.StoreState
import io.kvision.core.AlignItems
import io.kvision.core.JustifyContent
import io.kvision.html.*
import io.kvision.panel.VPanel
import io.kvision.panel.fieldsetPanel
import io.kvision.routing.routing
import io.kvision.utils.px
import kotlinx.coroutines.launch
import kotlinx.uuid.UUID

class CoursesPanel(
    goCourseClick: (UUID) -> Unit,
    storeState: StoreState,
    tokensDataStore: TokensDataStore,
    utils: UiUtils
) : VPanel(
    alignItems = AlignItems.CENTER,
    spacing = 10,
    justify = JustifyContent.CENTER
) {
    init {
        div("Добро пожаловать ${utils.getUserInfo(storeState)}!"){
            this.marginTop = 10.px
        }

        button("Выйти из аккаунта") {
            marginTop = 10.px
            width = 200.px
            alignItems = AlignItems.CENTER
            onClick {
                AppScope.launch {
                    tokensDataStore.clearTokens().let {
                        routing.navigate(RootUi.LOGIN.url)
                    }
                }
            }
        }

        div {
            h3("Доступные курсы:")
            storeState.listCourse?.forEach { course ->
                paddingTop = 10.px
                div {
                    paddingTop = 20.px
                    fieldsetPanel(course.name) {
                        paddingTop = 10.px
                        paddingBottom = 10.px
                        div("Описание курса: \n ${course.description}")
                        div {
                            paddingTop = 20.px
                            align = Align.CENTER
                            button(
                                text = "Пройти курс",
                                style = ButtonStyle.LIGHT
                            ) {
                                onClick {
                                    goCourseClick(course.courseInfoId)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}