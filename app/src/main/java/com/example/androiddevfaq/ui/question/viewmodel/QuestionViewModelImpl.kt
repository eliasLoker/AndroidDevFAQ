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
    //TODO("Попробовать хитро пробрасывать через геттеры и сеттеры поля")
    private var _title = ""
    private var _answer = ""


    override fun onActivityCreated() {
        viewModelScope.launch {
            when (val fetchQuestion = questionInteractor.getQuestion(questionID)) {
                is ResultWrapper.Success -> {
                    Log.d("QuestionDebug", "Success: ${fetchQuestion.data}")
                    _title = fetchQuestion.data.title
                    _answer = fetchQuestion.data.answer
                    title.value = _title
                    answer.value = _answer
                }

                is ResultWrapper.Error -> {
                    Log.d("QuestionDebug", "Error: ${fetchQuestion.exception.message}")
                }
            }
        }
    }

    override fun onAddFavouriteButtonClicked() {
        questionInteractor.addToFavouritesQuestion(questionID,_title, _answer)
    }
}