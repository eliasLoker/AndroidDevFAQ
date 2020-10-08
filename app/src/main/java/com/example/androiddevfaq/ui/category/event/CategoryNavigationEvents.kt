package com.example.androiddevfaq.ui.category.event

sealed class CategoryNavigationEvents {

    class GoToQuestionList(
        val categoryID: Int,
        val categoryName: String
    ) : CategoryNavigationEvents()
}