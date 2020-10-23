package com.example.androiddevfaq.ui.questions.model

import java.text.SimpleDateFormat
import java.util.*

class QuestionsItem {

    data class QuestionsItemSrc(
        val id: Int?,
        val name: String?,
        val priority: Int?,
        val rating: Int?,
        val timestamp: Long?
    )

    data class QuestionsItemDst(
        val questionID: Int?,
        val name: String?,
        val recyclerType: Int,
        val rating: Int?,
        val date: String,
        val timestamp: Long?
    )

    companion object {

        @JvmStatic
        fun QuestionsItem.QuestionsItemSrc.toQuestionItemRecycler(recyclerType: Int, date: String) =
            QuestionsItemDst(
                questionID = id,
                name = name,
                recyclerType = recyclerType,
                rating = rating,
                date = date,
                timestamp = timestamp
            )

        @JvmStatic
        fun getRecyclerType(index: Int) = when(index % 2 == 0){
            true -> 0
            false -> 1
        }

        @JvmStatic
        fun parseTimestampToDate(timestamp: Long)
                = SimpleDateFormat("dd-MM-yyyy HH:mm").format(Date(timestamp * 1000))
    }
}