package com.example.uiWebCourse.ui

import com.example.uiWebCourse.actions.StoreState
import com.example.uiWebCourse.models.CheckCourseModel
import com.example.uiWebCourse.models.CheckQuestionModel
import com.example.uiWebCourse.models.CheckQuestionModelString
import io.kvision.core.AlignItems
import io.kvision.core.JustifyContent
import io.kvision.form.check.RadioGroup
import io.kvision.form.formPanel
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.h4
import io.kvision.panel.VPanel
import io.kvision.panel.fieldsetPanel
import io.kvision.utils.px
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID

class QuestionsPanel(
    backClick: () -> Unit,
    checkCourse: (CheckCourseModel) -> Unit,
    storeState: StoreState
) : VPanel(
    alignItems = AlignItems.CENTER,
    spacing = 10,
    justify = JustifyContent.CENTER
) {

    private val courseInfoId = storeState.listQuestions?.firstOrNull()?.courseInfoId

    private val listForm = storeState.listQuestions?.map { course ->
        formPanel<CheckQuestionModelString> {
            add(
                key = CheckQuestionModelString::selectAnswerId,
                control = RadioGroup(
                    options = course.listAnswer.map { it.answerInfoId.toString() to it.answer },
                    label = "Варианты ответов:",
                ),
                required = true,
                requiredMessage = "Выберите ответ",
                validatorMessage = { "Выберите ответ" },
            ) {
                it.getValue()?.isNotEmpty() == true && it.getValue() != null
            }
        }

    }

    init {

        button(text = "Вернуться к списку курсов") {
            onClick {
                backClick()
            }
        }

        div {
            h4("Вопросы курса: ${storeState.nameSelectCourse}")
            storeState.listQuestions?.forEachIndexed { index, course ->
                listForm?.get(index)?.let { form ->
                    paddingTop = 20.px
                    fieldsetPanel(course.question) {
                        add(form)
                    }
                }
            }
        }

        button(text = "Проверить ответы") {
            width = 300.px
            onClick {

                listForm?.forEach {
                    it.validate()
                }

                listForm?.map { form ->
                    form.validate()
                }.let { list ->
                    if (list?.all { it } == true) {

                        var listCheckQuestion: List<CheckQuestionModel> = listOf()

                        if (courseInfoId != null) {
                            listCheckQuestion = listForm?.map { form ->
                                CheckQuestionModel(
                                    questionsInfoId = storeState.listQuestions?.get(listForm.indexOf(form))?.questionInfoId
                                        ?: UUID.generateUUID(),
                                    selectAnswerId = UUID(form.getData().selectAnswerId)
                                )
                            } ?: listOf()
                        }

                        storeState.listCourse?.firstOrNull()?.courseInfoId.let { id ->
                            if (id is UUID) {
                                checkCourse(CheckCourseModel(id, listCheckQuestion))
                            }
                        }
                    }
                }
            }
        }
    }
}