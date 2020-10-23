package com.example.androiddevfaq.model

import com.example.androiddevfaq.ui.category.model.CategoryItem
import com.example.androiddevfaq.ui.questions.model.QuestionsItem

class ResponseSrc {

    data class CategorySrc(
        val status: Boolean?,
        val categoryList: List<CategoryItem.CategoryItemSrc>?,
        val error: String?
    )

    data class QuestionListSrc(
        val status: Boolean?,
        val questionList: List<QuestionsItem.QuestionsItemSrc>?,
        val error: String?
    )

    data class QuestionSrc(
        val status: Boolean?,
        val title: String?,
        val answer: String?,
        val rating: Int?,
        val error: String?
    )

    data class AddQuestionSrc(
        val status: Boolean?,
        val message: String?,
        val error: String?
    )
}