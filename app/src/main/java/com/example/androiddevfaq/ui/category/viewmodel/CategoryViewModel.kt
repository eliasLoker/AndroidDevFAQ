package com.example.androiddevfaq.ui.category.viewmodel

import androidx.lifecycle.LiveData
import com.example.androiddevfaq.ui.category.adapter.CategoryAdapterListener
import com.example.androiddevfaq.ui.category.event.GoToNextFragmentEvents
import com.example.androiddevfaq.ui.category.event.SetCategoryAdapterEvent

interface CategoryViewModel : CategoryAdapterListener {

    val progressBarVisibility: LiveData<Boolean>

    val recyclerViewVisibility: LiveData<Boolean>

    val setAdapterEvent: LiveData<SetCategoryAdapterEvent>

    val swipeRefreshVisibility: LiveData<Boolean>

    val goToNextFragmentEvents: LiveData<GoToNextFragmentEvents>

    fun onActivityCreated()

    fun onSwipeRefreshLayout()
}