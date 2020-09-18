package com.example.androiddevfaq.utils.mapper

import com.example.androiddevfaq.model.CategoryResponse

class AdapterMapper {

    data class CategoryRecycler(
        val categoryName: String,
        val categorySize: String,
        val recyclerType: Int
    )

    companion object {

        @JvmStatic
        fun CategoryResponse.CategoryItemSrc.toCategoryRecycler(recyclerType: Int) = CategoryRecycler(
            categoryName = name ?: "Undefined name",
            categorySize = "Вопросов в категории: ${quantity ?: 0}",
            recyclerType = recyclerType
        )

        @JvmStatic
        fun CategoryResponse.CategoryItemSrc.getRecyclerType(index: Int) = when(index % 2 == 0){
            true -> 0
            false -> 1
        }
    }
}