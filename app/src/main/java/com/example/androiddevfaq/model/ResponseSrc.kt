package com.example.androiddevfaq.model

import com.example.androiddevfaq.model.CategoryResponse.CategoryItemSrc

class ResponseSrc {

    data class CategorySrc(
        val status: Boolean?,
        val categoryList: List<CategoryItemSrc>?,
        val error: String?
    )

    data class QuestionListSrc(
        val status: Boolean?,
        val questionList: List<QuestionListResponse.QuestionListItemSrc>?,
        val error: String?
    )

    data class QuestionSrc(
        val status: Boolean?,
        val title: String?,
        val answer: String?,
        val rating: Int?,
        val error: String?
    )

    data class AddQuestionResponse(
        val status: Boolean?,
        val message: String?,
        val error: String?
    )
}