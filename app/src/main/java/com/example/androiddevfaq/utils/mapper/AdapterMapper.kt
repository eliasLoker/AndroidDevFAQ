package com.example.androiddevfaq.utils.mapper

import com.example.androiddevfaq.model.CategoryResponse
import com.example.androiddevfaq.model.QuestionListResponse

class AdapterMapper {

    data class CategoryItemRecycler(
        val categoryID: Int,
        val categoryName: String,
        val size: Int,
        val titleSize: String,
        val priority: Int,
        val recyclerType: Int,
        val logoPath: String
    )

    data class QuestionItemRecycler(
        val questionID: Int,
        val name: String,
        val recyclerType: Int,
        val rating: Int
    )

    companion object {

        @JvmStatic
        fun CategoryResponse.CategoryItemSrc.toCategoryItemRecycler(recyclerType: Int) = CategoryItemRecycler(
            categoryID = id ?: 0,
            categoryName = name ?: "Undefined name",
            size = quantity ?: 0,
            titleSize = "Вопросов в категории: ${quantity ?: 0}",
            recyclerType = recyclerType,
            priority = priority ?: 0,
            logoPath = logoPath ?: ""
        )

        @JvmStatic
        fun getRecyclerType(index: Int) = when(index % 2 == 0){
            true -> 0
            false -> 1
        }

        @JvmStatic
        fun QuestionListResponse.QuestionListItemSrc.toQuestionItemRecycler(recyclerType: Int) = QuestionItemRecycler(
            questionID = id ?: 0,
            name = name ?: "",
            recyclerType = recyclerType,
            rating = rating ?: 0
        )
    }
}