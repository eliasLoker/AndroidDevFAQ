package com.example.androiddevfaq.ui.questionlist.viewmodel

import androidx.lifecycle.LiveData
import com.example.androiddevfaq.ui.questionlist.adapter.QuestionListListener
import com.example.androiddevfaq.ui.questionlist.event.GoToAddQuestionEvent
import com.example.androiddevfaq.ui.questionlist.event.GoToQuestionEvent
import com.example.androiddevfaq.ui.questionlist.event.SetQuestionListAdapterEvent

interface QuestionListViewModel : QuestionListListener {

    val initToolbar: LiveData<String>

    val progressBarVisibility: LiveData<Boolean>

    val swipeRefreshVisibility: LiveData<Boolean>

    val recyclerViewVisibility: LiveData<Boolean>

    val setQuestionListAdapterEvent: LiveData<SetQuestionListAdapterEvent>

    val goToQuestionEvent: LiveData<GoToQuestionEvent>

    val goToAddQuestionEvent: LiveData<GoToAddQuestionEvent>

    fun onActivityCreated()

    fun onSwipeRefreshLayout()

    fun onAddQuestionMenuClicked()
}