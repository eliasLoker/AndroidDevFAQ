package com.example.androiddevfaq.ui.category.model

class CategoryResponse {
//TODO("Delete class")
    data class CategoryItemSrc(
        val id: Int?,
        val name: String?,
        val quantity: Int?,
        val priority: Int?,
        val logoPath: String?,
        val lastQuestionDate: String?
    )

}