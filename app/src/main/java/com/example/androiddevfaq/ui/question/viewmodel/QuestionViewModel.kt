package com.example.androiddevfaq.ui.question.viewmodel

import androidx.lifecycle.LiveData

interface QuestionViewModel {

    val title: LiveData<String>

    val answer: LiveData<String>

    fun onActivityCreated()
}