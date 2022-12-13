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
                control = Text(
                    label = "Email"
                ),
                required = true,
                validatorMessage = { "Введите Email" }
            ) {
                it.getValue()?.isEmailValid()
            }
            add(
                key = RegisterUserModel::firstName,
                control = Text(label = "FirstName"),
                required = true
            ){
                it.getValue()?.isNotEmpty()
            }
            add(
                key = RegisterUserModel::lastName,
                control = Text(label = "LastName"),
                required = true
            ){
                it.getValue()?.isNotEmpty()
            }
            add(
                key = RegisterUserModel::patronymic,
                control = Password(label = "Patronymic"),
                required = false
            )
            add(
                key = RegisterUserModel::password,
                control = Password(label = "Password"),
                required = true
            ){
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