package com.example.androiddevfaq.ui.questionlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevfaq.ui.questionlist.interactor.QuestionListInteractor

class QuestionListFactory(
    private val categoryID: Int,
    private val categoryName: String,
    private val interactor: QuestionListInteractor
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionListViewModelImpl(categoryID, categoryName, interactor) as T
    }
}