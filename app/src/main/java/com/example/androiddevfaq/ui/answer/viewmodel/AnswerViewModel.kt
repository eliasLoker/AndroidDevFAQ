package com.example.androiddevfaq.ui.answer.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.androiddevfaq.base.BaseAction
import com.example.androiddevfaq.base.BaseViewModel
import com.example.androiddevfaq.base.BaseViewState
import com.example.androiddevfaq.ui.answer.interactor.AnswerInteractor
import com.example.androiddevfaq.utils.ResultWrapper
import kotlinx.coroutines.launch

class AnswerViewModel (
    private val isFromDatabase: Boolean,
    private val questionID: Int,
    private val answerInteractor: AnswerInteractor
) : BaseViewModel<AnswerViewModel.ViewState, AnswerViewModel.Action>(ViewState()) {

    private var _question: String? = ""
    private var _answer: String? = ""

    override fun onActivityCreated(isFirstLoading: Boolean) {
        if (isFirstLoading) {
            when(isFromDatabase) {
                true -> fetchFromDB()
                false -> fetchQuestionFromServer()
            }
        }
    }

    private fun fetchQuestionFromServer() {
        viewModelScope.launch {
            when (val fetchQuestion = answerInteractor.getQuestion(questionID)) {
                is ResultWrapper.Success -> {
                    Log.d("QuestionDebug", "Success: ${fetchQuestion.data}")
                    when(fetchQuestion.data.status ?: false) {
                        true -> {
                            _question = fetchQuestion.data.title
                            _answer = fetchQuestion.data.answer
                            sendAction(Action.Success(_question, _answer))
                        }
                        false -> {
                            sendAction(Action.Failure(fetchQuestion.data.error))
                        }
                    }

                }

                is ResultWrapper.Error -> {
                    sendAction(Action.Error)
                }
            }
        }
    }

    private fun fetchFromDB() {
        val question = answerInteractor.getPairQuestion(questionID)
        sendAction(Action.Success(question.first, question.second))
    }

    fun onAddQuestionButtonClicked() {
        val question = _question ?: return
        val answer = _answer ?: return
        answerInteractor.addToFavouritesQuestion(
            questionID,
            question,
            answer
        )
    }

    override fun onReduceState(viewAction: Action): ViewState {
        return when(viewAction) {
            is Action.Success
            -> state.copy(
                progressBarVisibility = false,
                questionText = viewAction.questionText,
                answerText = viewAction.answerText,
                questionVisibility = true,
                answerVisibility = true
            )
            is Action.Failure
            -> state.copy(
                progressBarVisibility = false,
                errorText = viewAction.failureMessage,
                errorTextViewVisibility = true,
                questionVisibility = false,
                answerVisibility = false
            )
            is Action.Error
            -> state.copy(
                progressBarVisibility = false,
                errorText = null,
                errorTextViewVisibility = true,
                questionVisibility = false,
                answerVisibility = false
            )
        }
    }

    data class ViewState(
        val progressBarVisibility: Boolean = true,
        val questionText: String? = "",
        val questionVisibility: Boolean = false,
        val answerText: String? = "",
        val answerVisibility: Boolean = false,
        val errorText: String? = "",
        val errorTextViewVisibility: Boolean = false
    ) : BaseViewState

    sealed class Action : BaseAction {

        class Success(
            val questionText: String?,
            val answerText: String?
        ) : Action()

        class Failure(
            val failureMessage: String?
        ) : Action()

        object Error: Action()
    }
}