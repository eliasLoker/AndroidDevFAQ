package com.example.androiddevfaq.utils.mapper

import com.example.androiddevfaq.model.CategoryResponse

class AdapterMapper {

    data class CategoryRecycler(
        val categoryID: Int,
        val categoryName: String,
        val categorySize: String,
        val recyclerType: Int
    )

    companion object {

        @JvmStatic
        fun CategoryResponse.CategoryItemSrc.toCategoryRecycler(recyclerType: Int) = CategoryRecycler(
            categoryID = id ?: 0,
            categoryName = name ?: "Undefined name",
            categorySize = "Вопросов в категории: ${quantity ?: 0}",
            recyclerType = recyclerType
        )

        @JvmStatic
        fun getRecyclerType(index: Int) = when(index % 2 == 0){
            true -> 0
            false -> 1
        }
    }
}