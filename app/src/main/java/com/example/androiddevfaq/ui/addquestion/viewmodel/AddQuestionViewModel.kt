package com.example.androiddevfaq.ui.addquestion.viewmodel

import androidx.lifecycle.LiveData
import com.example.androiddevfaq.ui.addquestion.event.SetErrorEvents
import com.example.androiddevfaq.ui.addquestion.event.ShowAddResultDialogEvents

interface AddQuestionViewModel {

    val setToolbar: LiveData<String>

    val setErrorEvents: LiveData<SetErrorEvents>

    val showAddResultDialogEvents: LiveData<ShowAddResultDialogEvents>

    val goToBack: LiveData<Any>

    fun onActivityCreated()

    fun onQuestionTextChanged(question: String)

    fun onAnswerTextChanged(answer: String)

    fun onSendQuestionButtonClicked()

    fun onDialogPositiveButtonClicked()
}