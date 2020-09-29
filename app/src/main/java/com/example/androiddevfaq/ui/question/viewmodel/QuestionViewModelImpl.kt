package com.example.androiddevfaq.ui.question.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevfaq.ui.question.interactor.QuestionInteractor
import com.example.androiddevfaq.utils.ResultWrapper
import kotlinx.coroutines.launch

class QuestionViewModelImpl(
    private val questionID: Int,
    private val questionInteractor: QuestionInteractor
) : ViewModel(), QuestionViewModel {

    override val title = MutableLiveData<String>()
    override val answer = MutableLiveData<String>()

    override fun onActivityCreated() {
        viewModelScope.launch {
            when (val fetchQuestion = questionInteractor.getQuestion(questionID)) {
                is ResultWrapper.Success -> {
                    Log.d("QuestionDebug", "Success: ${fetchQuestion.data}")
                    title.value = fetchQuestion.data.title
                    answer.value = fetchQuestion.data.answer
                }

                is ResultWrapper.Error -> {
                    Log.d("QuestionDebug", "Error: ${fetchQuestion.exception.message}")
                }
            }
        }
    }
}