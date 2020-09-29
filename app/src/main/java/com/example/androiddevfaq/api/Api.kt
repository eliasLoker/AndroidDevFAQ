package com.example.androiddevfaq.api

import com.example.androiddevfaq.model.ResponseSrc

interface Api {

    suspend fun getCategories(
        isError: Boolean = false
    ): ResponseSrc.CategorySrc

    suspend fun getQuestionListByID(
        categoryID: Int,
        isError: Boolean = false
    ): ResponseSrc.QuestionListSrc

    suspend fun getQuestion(
        questionID: Int,
        isError: Boolean = false
    ) : ResponseSrc.QuestionSrc

    suspend fun addQuestion(
        categoryID: Int,
        question: String,
        answer: String,
        isError: Boolean = false
    ) : ResponseSrc.AddQuestionResponse
}