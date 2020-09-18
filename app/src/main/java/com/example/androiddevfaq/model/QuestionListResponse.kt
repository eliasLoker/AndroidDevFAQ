package com.example.androiddevfaq.model

class QuestionListResponse {

    data class QuestionListItemSrc(
        val id: Int?,
        val name: String?,
        val quantity: Int?,
        val priority: Int?
    )
}