package com.example.androiddevfaq.ui.addquestion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevfaq.ui.addquestion.interactor.AddQuestionInteractor

class AddQuestionFactory(
    private val categoryID: Int,
    private val categoryName: String,
    private val addQuestionInteractor: AddQuestionInteractor
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddQuestionViewModel(categoryID, categoryName, addQuestionInteractor) as T
    }
}