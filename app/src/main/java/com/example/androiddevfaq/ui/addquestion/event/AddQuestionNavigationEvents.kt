package com.example.androiddevfaq.ui.addquestion.event

sealed class AddQuestionNavigationEvents {

    class ShowSuccessDialog(val message: String?) : AddQuestionNavigationEvents()

    class ShowFailureDialog(val failureMessage: String) : AddQuestionNavigationEvents()

    object ShowErrorDialog : AddQuestionNavigationEvents()

    object GoToBack : AddQuestionNavigationEvents()
}