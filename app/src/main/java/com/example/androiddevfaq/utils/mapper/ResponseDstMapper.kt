package com.example.androiddevfaq.utils.mapper

import com.example.androiddevfaq.model.CategoryResponse
import com.example.androiddevfaq.model.QuestionListResponse
import com.example.androiddevfaq.model.ResponseSrc

class ResponseDstMapper {

    data class CategoryDst(
        val status: Boolean,
        val categoryList: List<CategoryResponse.CategoryItemSrc>,
        val error: String
    )

    data class QuestionListDst(
        val status: Boolean,
        val questionList: List<QuestionListResponse.QuestionListItemSrc>,
        val error: String
    )

    data class QuestionDst(
        val status: Boolean,
        val title: String,
        val answer: String,
        val rating: Int
    )

    data class AddQuestionDst(
        val status: Boolean,
        val message: String,
        val error: String
    )

    companion object {

        @JvmStatic
        fun ResponseSrc.CategorySrc.toCategoryResponseDst() = CategoryDst(
            status = status ?: false,
            categoryList = categoryList ?: emptyList(),
            error = error ?: "Неизвестная ошибка"
        )

        @JvmStatic
        fun ResponseSrc.QuestionListSrc.toQuestionListResponseDst() = QuestionListDst(
            status = status ?: false,
            questionList = questionList ?: emptyList(),
            error = error ?: "Неизвестная ошибка"
        )

        @JvmStatic
        fun ResponseSrc.QuestionSrc.toQuestionDst() = QuestionDst(
            status = status ?: false,
            title = title ?: "Error",
            answer = answer ?: "Error",
            rating = rating ?: 0
        )

        fun ResponseSrc.AddQuestionResponse.toAddQuestionDst() = AddQuestionDst(
            status = status ?: false,
            message = message ?: "",
            error = error ?: "Не удалось добавить вопрос, попробуйте позже"
        )
    }
}