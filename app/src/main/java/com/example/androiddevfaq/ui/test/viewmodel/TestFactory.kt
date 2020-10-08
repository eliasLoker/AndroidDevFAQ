package com.example.androiddevfaq.ui.test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevfaq.ui.test.interactor.TestInteractor

class TestFactory(
    private val categoryID: Int,
    private val categoryName: String,
    private val testInteractor: TestInteractor
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TestViewModelImpl(categoryID, categoryName, testInteractor) as T
    }
}