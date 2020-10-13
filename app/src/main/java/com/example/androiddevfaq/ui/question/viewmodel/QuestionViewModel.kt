package com.example.androiddevfaq.ui.question.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.androiddevfaq.base.BaseAction
import com.example.androiddevfaq.base.BaseViewModel
import com.example.androiddevfaq.base.BaseViewState
import com.example.androiddevfaq.ui.question.interactor.QuestionInteractor
import com.example.androiddevfaq.utils.ResultWrapper
import kotlinx.coroutines.launch

class QuestionViewModel (
    private val isFromDatabase: Boolean,
    private val questionID: Int,
    private val questionInteractor: QuestionInteractor
) : BaseViewModel<QuestionViewModel.ViewState, QuestionViewModel.Action>(ViewState()) {

    private var _question = ""
    private var _answer = ""

    override fun onActivityCreated(isFirstLoading: Boolean) {
        if (isFirstLoading) {
            when(isFromDatabase) {
                true -> {
                    val question = questionInteractor.getPairQuestion(questionID)
                    sendAction(Action.SuccessLoading(question.first, question.second))
                }
                false -> getQuestionFromServer()
            }
        }
    }

    private fun getQuestionFromServer() {
        viewModelScope.launch {
            when (val fetchQuestion = questionInteractor.getQuestion(questionID)) {
                is ResultWrapper.Success -> {
                    Log.d("QuestionDebug", "Success: ${fetchQuestion.data}")
                    when(fetchQuestion.data.status) {
                        true -> {
                            _question = fetchQuestion.data.title
                            _answer = fetchQuestion.data.answer
                            sendAction(Action.SuccessLoading(_question, _answer))
                        }
                        false -> {

                        }
                    }

                }

                is ResultWrapper.Error -> {
                    Log.d("QuestionDebug", "Error: ${fetchQuestion.exception.message}")
                }
            }
        }
    }

    fun onAddQuestionButtonClicked() {
        questionInteractor.addToFavouritesQuestion(
            questionID,
            _question,
            _answer
        )
    }

    override fun onReduceState(viewAction: Action): ViewState {
        return when(viewAction) {
            is Action.SuccessLoading
            -> state.copy(
                progressBarVisibility = false,
                questionText = viewAction.questionText,
                answerText = viewAction.answerText,
                questionVisibility = true,
                answerVisibility = true
            )
        }
    }

    data class ViewState(
        val progressBarVisibility: Boolean = true,
        val questionText: String = "",
        val answerText: String = "",
        val questionVisibility: Boolean = false,
        val answerVisibility: Boolean = false
    ) : BaseViewState

    sealed class Action : BaseAction {

        class SuccessLoading(
            val questionText: String,
            val answerText: String
        ) : Action()
    }
}