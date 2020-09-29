package com.example.androiddevfaq.ui.category.event

sealed class GoToNextFragmentEvents {

    class QuestionList(val categoryID: Int, val categoryName: String) : GoToNextFragmentEvents()
}