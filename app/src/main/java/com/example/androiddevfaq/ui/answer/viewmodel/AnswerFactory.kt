package com.example.androiddevfaq.ui.answer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevfaq.ui.answer.interactor.AnswerInteractor

class AnswerFactory(
    private val isFromDatabase: Boolean,
    private val questionID: Int,
    private val answerInteractor: AnswerInteractor
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AnswerViewModel(isFromDatabase, questionID, answerInteractor) as T
    }
}