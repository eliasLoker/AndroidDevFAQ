package com.example.androiddevfaq.ui.questions.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevfaq.ui.questions.interactor.QuestionsInteractor

class QuestionsFactory(
    private val subTitle: String,
    private val categoryID: Int,
    private val categoryName: String,
    private val interactor: QuestionsInteractor
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionsViewModel(subTitle, categoryID, categoryName, interactor) as T
    }
}