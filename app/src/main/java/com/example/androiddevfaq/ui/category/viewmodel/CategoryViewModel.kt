package com.example.androiddevfaq.ui.category.viewmodel

import androidx.lifecycle.LiveData
import com.example.androiddevfaq.ui.category.adapter.CategoryAdapterListener
import com.example.androiddevfaq.ui.category.event.GoToNextFragmentEvents
import com.example.androiddevfaq.ui.category.event.SetCategoryAdapterEvent

interface CategoryViewModel : CategoryAdapterListener {

    val initToolbar: LiveData<Any>

    val progressBarVisibility: LiveData<Boolean>

    val recyclerViewVisibility: LiveData<Boolean>

    val setAdapterEvent: LiveData<SetCategoryAdapterEvent>

    val swipeRefreshVisibility: LiveData<Boolean>

    val goToNextFragmentEvents: LiveData<GoToNextFragmentEvents>

    val initSortSpinner: LiveData<Any>

    val sortSpinnerVisibility: LiveData<Boolean>

    fun onActivityCreated()

    fun onSwipeRefreshLayout()

    fun onSortSpinnerItemSelected(position: Int)
}