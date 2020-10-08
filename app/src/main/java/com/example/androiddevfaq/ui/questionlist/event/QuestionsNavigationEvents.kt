package com.example.androiddevfaq.ui.questionlist.event

sealed class QuestionsNavigationEvents {
    class ToQuestion(
        val questionID: Int
    ) : QuestionsNavigationEvents()

    class ToAddQuestion(
        val categoryID: Int,
        val categoryName: String
    ) : QuestionsNavigationEvents()
}