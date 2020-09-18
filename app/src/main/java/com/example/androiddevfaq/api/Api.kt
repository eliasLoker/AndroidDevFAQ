package com.example.androiddevfaq.api

import com.example.androiddevfaq.model.ResponseSrc

interface Api {

    suspend fun getCategories(isError: Boolean = false, isEnableDelay: Boolean = true) : ResponseSrc.CategorySrc

    suspend fun getQuestionList() : ResponseSrc.QuestionListSrc
}