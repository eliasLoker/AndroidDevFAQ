package com.example.androiddevfaq.ui.addquestion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevfaq.ui.addquestion.event.SetErrorEvents
import com.example.androiddevfaq.ui.addquestion.event.ShowAddResultDialogEvents
import com.example.androiddevfaq.ui.addquestion.interactor.AddQuestionInteractor
import com.example.androiddevfaq.utils.ResultWrapper
import com.example.androiddevfaq.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class AddQuestionViewModelImpl(
    private val categoryID: Int,
    private val categoryName: String,
    private val addQuestionInteractor: AddQuestionInteractor
) : ViewModel(), AddQuestionViewModel {

    override val setToolbar = MutableLiveData<String>()
    override val setErrorEvents = SingleLiveEvent<SetErrorEvents>()
    override val showAddResultDialogEvents = SingleLiveEvent<ShowAddResultDialogEvents>()
    override val goToBack = SingleLiveEvent<Any>()
    private var _question = ""
    private var _answer = ""

    override fun onActivityCreated() {
        setToolbar.value = categoryName
    }

    override fun onQuestionTextChanged(question: String) {
        _question = question
    }

    override fun onAnswerTextChanged(answer: String) {
        _answer = answer
    }

    override fun onSendQuestionButtonClicked() {
        if (_question == "") {
            setErrorEvents.value = SetErrorEvents.EmptyQuestion
            return
        }

        if (_question.contains("?")) {
            setErrorEvents.value = SetErrorEvents.NotCorrectQuestion
            return
        }

        if (_answer == "") {
            setErrorEvents.value = SetErrorEvents.EmptyAnswer
            return
        }
        makeRequestAddQuestion()
    }

    override fun onDialogPositiveButtonClicked() {
        goToBack.value = Any()
    }

    private fun makeRequestAddQuestion() {
        viewModelScope.launch {
            when(val addQuestion = addQuestionInteractor.addQuestion(
                categoryID, _question, _answer
            )) {
                is ResultWrapper.Success -> {
                    when(addQuestion.data.status) {
                        true -> {
                            showAddResultDialogEvents.value = ShowAddResultDialogEvents.Success(addQuestion.data.message)
                        }
                        false -> {

                        }
                    }
                }

                is ResultWrapper.Error -> {

                }
            }
        }
    }
}