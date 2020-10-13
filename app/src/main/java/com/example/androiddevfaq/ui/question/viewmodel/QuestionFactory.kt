package com.example.androiddevfaq.ui.question.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevfaq.ui.question.interactor.QuestionInteractor

class QuestionFactory(
    private val isFromDatabase: Boolean,
    private val questionID: Int,
    private val questionInteractor: QuestionInteractor
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionViewModel(isFromDatabase, questionID, questionInteractor) as T
    }
}