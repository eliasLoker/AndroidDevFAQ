package com.example.androiddevfaq.utils.mapper

import com.example.androiddevfaq.model.CategoryResponse
import com.example.androiddevfaq.model.ResponseSrc

class ResponseDstMapper {

    data class CategoryResponseDst(
        val status: Boolean,
        val categoryList: List<CategoryResponse.CategoryItemSrc>,
        val error: String
    )

    companion object {

        @JvmStatic
        fun ResponseSrc.CategorySrc.toCategoryResponseDst() = CategoryResponseDst(
            status = status ?: false,
            categoryList = categoryList ?: emptyList(),
            error = error ?: "Неизвестная ошибка"
        )
    }
}