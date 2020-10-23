package com.example.androiddevfaq.ui.addquestion.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.androiddevfaq.base.BaseAction
import com.example.androiddevfaq.base.BaseViewModel
import com.example.androiddevfaq.base.BaseViewState
import com.example.androiddevfaq.ui.addquestion.event.AddQuestionNavigationEvents
import com.example.androiddevfaq.ui.addquestion.interactor.AddQuestionInteractor
import com.example.androiddevfaq.utils.ResultWrapper
import com.example.androiddevfaq.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class AddQuestionViewModel(
    private val title: String,
    private val categoryID: Int,
    private val categoryName: String,
    private val addQuestionInteractor: AddQuestionInteractor
) : BaseViewModel<AddQuestionViewModel.ViewState, AddQuestionViewModel.Action>(
    ViewState()
) {

    val navigationEvents = SingleLiveEvent<AddQuestionNavigationEvents>()

    private var _question = ""
    private var _answer = ""

    override fun onActivityCreated(isFirstLoading: Boolean) {
//        sendAction(Action.SetSubtitleToolbar(categoryName))
        sendAction(Action.NotLoading)
    }

    fun onQuestionTextChanged(question: String) {
        _question = question
    }


    fun onAnswerTextChanged(answer: String) {
        _answer = answer
    }

    fun onSendQuestionButtonClicked() {
        makeRequestAddQuestion()
    }

    fun onDialogPositiveButtonsClicked() {
        navigationEvents.value = AddQuestionNavigationEvents.GoToBack
    }

    private fun makeRequestAddQuestion() {
        sendAction(Action.Loading)
        viewModelScope.launch {
            when(val addQuestion = addQuestionInteractor.addQuestion(
                categoryID, _question, _answer
            )) {
                is ResultWrapper.Success -> {
                    when(addQuestion.data.status ?: false) {
                        true -> {
                            val event = AddQuestionNavigationEvents.ShowSuccessDialog(addQuestion.data.message)
                            navigationEvents.value = event
                        }
                        false -> {
                            val event = AddQuestionNavigationEvents.ShowFailureDialog(addQuestion.data.error)
                            navigationEvents.value = event
                        }
                    }
                    sendAction(Action.NotLoading)
                }

                is ResultWrapper.Error -> {
                    sendAction(Action.NotLoading)
                    val event = AddQuestionNavigationEvents.ShowErrorDialog
                    navigationEvents.value = event
                }
            }
        }
    }

    override fun onReduceState(viewAction: Action) = when(viewAction) {
        is Action.Loading -> state.copy(
            progressBarVisibility = true,
            questionEditTextVisibility = false,
            answerEditTextVisibility= false,
            sendQuestionButtonVisibility = false
        )

        is Action.NotLoading -> state.copy(
            titleText = title,
            subtitleText = categoryName,
            progressBarVisibility = false,
            questionEditTextVisibility = true,
            answerEditTextVisibility= true,
            sendQuestionButtonVisibility = true
        )
    }

    data class ViewState(
        val titleText: String = "",
        val subtitleText: String = "",
        val progressBarVisibility: Boolean = true,
        val questionEditTextVisibility: Boolean = false,
        val answerEditTextVisibility: Boolean = false,
        val sendQuestionButtonVisibility: Boolean = false
    ) : BaseViewState

    sealed class Action : BaseAction {

        object Loading : Action()

        object NotLoading: Action()
    }
}