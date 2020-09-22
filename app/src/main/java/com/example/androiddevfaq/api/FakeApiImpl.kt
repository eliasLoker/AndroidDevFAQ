package com.example.androiddevfaq.api

import com.example.androiddevfaq.model.CategoryResponse
import com.example.androiddevfaq.model.QuestionListResponse
import com.example.androiddevfaq.model.ResponseSrc
import kotlinx.coroutines.delay

class FakeApiImpl : Api {

    override suspend fun getCategories(
        isError: Boolean,
        isEnableDelay: Boolean
    ): ResponseSrc.CategorySrc {
        if (isEnableDelay) delay(2000)
        when (isError) {
            true -> {
                throw NoSuchFieldException("Parsing error")
            }
            false -> {
                val categoryList = ArrayList<CategoryResponse.CategoryItemSrc>()
                categoryList.add(CategoryResponse.CategoryItemSrc(0, "Base Java", 2, 2))
                categoryList.add(CategoryResponse.CategoryItemSrc(1, "Base Kotlin", 3, 0))
                categoryList.add(CategoryResponse.CategoryItemSrc(2, "Android", 1, 1))
                return ResponseSrc.CategorySrc(true, categoryList, null)
            }
        }
    }

    override suspend fun getQuestionListByID(
        categoryID: Int,
        isError: Boolean,
        isEnableDelay: Boolean
    ): ResponseSrc.QuestionListSrc {
        if (isEnableDelay) delay(2000)
        return when (isError) {
            true -> {
                throw NoSuchFieldException("Parsing error")
            }
            false -> {
                when (categoryID) {
                    0 -> {
                        val questionsList = ArrayList<QuestionListResponse.QuestionListItemSrc>()
                        questionsList.add(
                            QuestionListResponse.QuestionListItemSrc(
                                0,
                                "First question",
                                1
                            )
                        )
                        questionsList.add(
                            QuestionListResponse.QuestionListItemSrc(
                                1,
                                "Second question",
                                0
                            )
                        )
                        return ResponseSrc.QuestionListSrc(true, questionsList, null)
                    }
                    1 -> {
                        val questionsList = ArrayList<QuestionListResponse.QuestionListItemSrc>()
                        questionsList.add(
                            QuestionListResponse.QuestionListItemSrc(
                                0,
                                "First question",
                                1
                            )
                        )
                        questionsList.add(
                            QuestionListResponse.QuestionListItemSrc(
                                1,
                                "Second question",
                                0
                            )
                        )
                        questionsList.add(
                            QuestionListResponse.QuestionListItemSrc(
                                2,
                                "Third question",
                                2
                            )
                        )
                        return ResponseSrc.QuestionListSrc(true, questionsList, null)
                    }
                    2 -> {
                        val questionsList = ArrayList<QuestionListResponse.QuestionListItemSrc>()
                        questionsList.add(
                            QuestionListResponse.QuestionListItemSrc(
                                0,
                                "First question",
                                1
                            )
                        )
                        return ResponseSrc.QuestionListSrc(true, questionsList, null)
                    }
                    else -> ResponseSrc.QuestionListSrc(false, emptyList(), "Category not exist")
                }
            }
        }
    }
}