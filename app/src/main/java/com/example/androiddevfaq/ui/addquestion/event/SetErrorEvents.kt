package com.example.androiddevfaq.ui.addquestion.event

sealed class SetErrorEvents {

    object EmptyQuestion : SetErrorEvents()

    object NotCorrectQuestion : SetErrorEvents()

    object EmptyAnswer : SetErrorEvents()
}