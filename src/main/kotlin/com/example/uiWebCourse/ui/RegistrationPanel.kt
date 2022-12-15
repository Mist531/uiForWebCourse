package com.example.uiWebCourse.ui

import com.example.uiWebCourse.RootUi
import com.example.uiWebCourse.isEmailValid
import com.example.uiWebCourse.models.RegisterUserModel
import io.kvision.form.formPanel
import io.kvision.form.text.Password
import io.kvision.form.text.Text
import io.kvision.html.button
import io.kvision.panel.VPanel
import io.kvision.routing.routing
import io.kvision.utils.px

class RegistrationPanel(
    registrationClick: (RegisterUserModel) -> Unit
) : VPanel() {
    init {
        addCssStyle(signInUpPanelStyle)
        val formPanel = formPanel {
            width = 300.px

            add(
                key = RegisterUserModel::login,
                control = Text(label = "Email"),
                required = true,
                validatorMessage = { "Введите Email" },
                requiredMessage = "Введите Email"
            ) {
                it.getValue()?.isEmailValid()
            }

            add(
                key = RegisterUserModel::lastName,
                control = Text(label = "Фамилия"),
                required = true,
                validatorMessage = { "Введите Фамилия" },
                requiredMessage = "Введите Фамилия"
            ) {
                it.getValue()?.isNotEmpty()
            }

            add(
                key = RegisterUserModel::firstName,
                control = Text(label = "Имя"),
                required = true,
                validatorMessage = { "Введите Имя" },
                requiredMessage = "Введите Имя"
            ) {
                it.getValue()?.isNotEmpty()
            }

            add(
                key = RegisterUserModel::patronymic,
                control = Text(label = "Оттество"),
                required = false,
                validatorMessage = { "Введите Оттество" },
                requiredMessage = "Введите Оттество"
            )

            add(
                key = RegisterUserModel::password,
                control = Password(label = "Пароль"),
                required = true,
                validatorMessage = { "Введите Пароль" },
                requiredMessage = "Введите Пароль"
            ) {
                it.getValue()?.isNotEmpty()
            }
        }
        formPanel.add(
            VPanel {

                button(text = "Зарегистрироваться") {
                    width = 300.px
                    marginBottom = 10.px
                    onClick {
                        val validation = formPanel.validate()
                        val registerUserModel = formPanel.getData()
                        console.log(registerUserModel.toString())
                        console.log(validation.toString())
                        if (validation) {
                            registrationClick(registerUserModel)
                        }
                    }
                }

                button(text = "Войти") {
                    onClick {
                        routing.kvNavigate(RootUi.LOGIN.url)
                    }
                }
            }
        )
    }
}