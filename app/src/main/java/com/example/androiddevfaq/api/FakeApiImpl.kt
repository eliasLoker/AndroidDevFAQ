package com.example.androiddevfaq.api

import com.example.androiddevfaq.model.ResponseSrc
import com.example.androiddevfaq.model.CategoryResponse
import com.example.androiddevfaq.model.QuestionListResponse
import kotlinx.coroutines.delay
import java.lang.NumberFormatException
import java.text.ParseException

class FakeApiImpl: Api {

    override suspend fun getCategories(
        isError: Boolean,
        isEnableDelay: Boolean
    ): ResponseSrc.CategorySrc {
        if (isEnableDelay) delay(2000)
        when(isError) {
            true -> {
                throw NoSuchFieldException("Parsing error")
            }
            false -> {
                val categoryList = ArrayList<CategoryResponse.CategoryItemSrc>()
                categoryList.add(CategoryResponse.CategoryItemSrc(0, "Base java", 5, 0))
                categoryList.add(CategoryResponse.CategoryItemSrc(1, "Base Kotlin", 7, 1))
                categoryList.add(CategoryResponse.CategoryItemSrc(2, "Android", 7, 2))
                return ResponseSrc.CategorySrc(true, categoryList, null)
            }
        }
    }

    override suspend fun getQuestionList(): ResponseSrc.QuestionListSrc {
        val questionsList = ArrayList<QuestionListResponse.QuestionListItemSrc>()
        questionsList.add(QuestionListResponse.QuestionListItemSrc(0, "Base java", 5, 0))
        questionsList.add(QuestionListResponse.QuestionListItemSrc(1, "Base Kotlin", 7, 1))
        questionsList.add(QuestionListResponse.QuestionListItemSrc(2, "Android", 7, 2))
        return ResponseSrc.QuestionListSrc(true, questionsList, null)
    }
}