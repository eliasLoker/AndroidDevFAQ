package com.example.androiddevfaq.ui.favourites.model

import com.example.androiddevfaq.database.FavouriteQuestion

class FavouriteItem {

    data class FavouriteItemDst(
        val questionID: Int,
        val title: String,
        val recyclerType: Int,
    )

    companion object {

        @JvmStatic
        fun getRecyclerType(index: Int) = when(index % 2 == 0){
            true -> 0
            false -> 1
        }

        @JvmStatic
        fun FavouriteQuestion.toFavouriteItemDst(recyclerType: Int) =
            FavouriteItemDst(
                questionID = id,
                title = question,
                recyclerType = recyclerType
            )
    }
}