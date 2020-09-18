package com.example.androiddevfaq.ui.category.viewmodel

import androidx.lifecycle.LiveData
import com.example.androiddevfaq.ui.category.event.SetCategoryAdapterEvent

interface CategoryViewModel {

    val progressBarVisibility: LiveData<Boolean>

    val setAdapterEvent: LiveData<SetCategoryAdapterEvent>

    fun onActivityCreated()
}