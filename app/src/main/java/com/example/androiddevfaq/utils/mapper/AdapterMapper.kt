package com.example.androiddevfaq.utils.mapper

import android.util.Log
import com.example.androiddevfaq.database.FavouriteQuestion
import com.example.androiddevfaq.model.CategoryResponse
import com.example.androiddevfaq.model.QuestionListResponse
import java.text.SimpleDateFormat
import java.util.*

class AdapterMapper {

    data class CategoryItemRecycler(
        val categoryID: Int,
        val categoryName: String,
        val size: Int,
        val titleSize: String,
        val priority: Int,
        val recyclerType: Int,
        val logoPath: String,
        val lastQuestionDate: String
    )

    data class QuestionItemRecycler(
        val questionID: Int,
        val name: String,
        val recyclerType: Int,
        val rating: Int,
        val date: String,
        val timestamp: Long
    )

    data class FavouriteItemRecycler(
        val questionID: Int,
        val title: String,
        val recyclerType: Int,
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
            logoPath = logoPath ?: "",
            lastQuestionDate = lastQuestionDate ?: ""
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
            rating = rating ?: 0,
            date = parseTimestampToDate(timestamp ?: 0),
            timestamp = timestamp ?: 0
        )

//        fun parseTimestampToDate(timestamp: Long) : String {
//            Log.d("DateDebug", "TS PARSE: $timestamp")
//            val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
//            val date = Date(timestamp * 1000)
//            return simpleDateFormat.format(date)
//        }

        fun parseTimestampToDate(timestamp: Long)
                = SimpleDateFormat("dd-MM-yyyy HH:mm").format(Date(timestamp * 1000))

        @JvmStatic
        fun FavouriteQuestion.toFavouriteItemRecycler(recyclerType: Int) = FavouriteItemRecycler(
            questionID = id,
            title =  question,
            recyclerType = recyclerType
        )
    }
}