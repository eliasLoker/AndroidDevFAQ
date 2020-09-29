package com.example.androiddevfaq.ui.question.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevfaq.ui.question.interactor.QuestionInteractor

class QuestionFactory(
    private val questionID: Int,
    private val questionInteractor: QuestionInteractor
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionViewModelImpl(questionID, questionInteractor) as T
    }
}