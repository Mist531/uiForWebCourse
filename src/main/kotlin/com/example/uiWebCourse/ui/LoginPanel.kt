package com.example.uiWebCourse.ui

import com.example.uiWebCourse.RootUi
import com.example.uiWebCourse.isEmailValid
import com.example.uiWebCourse.models.LoginModel
import io.kvision.core.AlignItems
import io.kvision.core.Display
import io.kvision.core.JustifyContent
import io.kvision.core.Style
import io.kvision.form.formPanel
import io.kvision.form.text.Text
import io.kvision.html.button
import io.kvision.panel.VPanel
import io.kvision.routing.routing
import io.kvision.utils.perc
import io.kvision.utils.px
import io.kvision.utils.vh

val signInUpPanelStyle = Style {
    width = 100.perc
    height = 100.vh
    display = Display.FLEX
    justifyContent = JustifyContent.CENTER
    alignItems = AlignItems.CENTER
}

class LoginPanel(
    loginClick: (LoginModel) -> Unit
) : VPanel() {
    init {

        addCssStyle(signInUpPanelStyle)

        val formPanel = formPanel {
            width = 300.px
            add(
                key = LoginModel::login,
                control = Text(label = "Email"),
                required = true,
                validatorMessage = { "Введите Email" },
                requiredMessage = "Введите Email",
            ) {
                it.getValue()?.isEmailValid() == true || it.getValue() == "admin"
            }
            add(
                key = LoginModel::password,
                control = Text(label = "Пароль"),
                required = true,
                validatorMessage = { "Введите Пароль" },
                requiredMessage = "Введите Пароль",
            ) {
                it.getValue()?.isNotEmpty()
            }
        }

        formPanel.add(
            VPanel {

                button(text = "Войти") {
                    width = 300.px
                    onClick {
                        val validation = formPanel.validate()
                        val loginModel = formPanel.getData()
                        console.log(loginModel.toString())
                        console.log(validation.toString())
                        if (validation) {
                            console.log("Валидация прошла успешно")
                            console.log("loginModel: $loginModel")
                            loginClick(loginModel)
                        }
                    }
                }

                button(text = "Зарегистрироваться") {
                    width = 100.perc
                    marginTop = 10.px
                    onClick {
                        routing.kvNavigate(RootUi.REGISTER.url)
                    }
                }
            }
        )
    }
}