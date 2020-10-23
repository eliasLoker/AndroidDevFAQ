package com.example.androiddevfaq.ui.category.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevfaq.ui.category.interactor.CategoryInteractor

class CategoryFactory(
    private val title: String,
    private val subTitle: String,
    private val categoryInteractor: CategoryInteractor
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoryViewModel(title, subTitle, categoryInteractor) as T
    }
}