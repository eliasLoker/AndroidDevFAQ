package com.example.androiddevfaq.ui.addquestion.event

sealed class ShowAddResultDialogEvents {

    class Success(
        val message: String
    ): ShowAddResultDialogEvents()

    class Error(
        val errorMessage: String
    ): ShowAddResultDialogEvents()
}