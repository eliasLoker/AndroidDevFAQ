package com.example.androiddevfaq.ui.favourites.interactor

import com.example.androiddevfaq.database.FavouriteQuestion
import io.realm.Realm

class FavouritesInteractor(
    private val realm: Realm
) {

    fun getFavourites() = realm.where(FavouriteQuestion::class.java)
        .findAll()
        .sort("addDateTimestamp")
        .toMutableList()

//        fun getFavourites() : List<FavouriteQuestion> {
//            return realm.where(FavouriteQuestion::class.java).findAll().sort("addDateTimestamp").toMutableList()
//        realm.use {
//            it.executeTransaction {
//                val favourites = it.where(FavouriteQuestion::class.java).findAll().sort("addDateTimestamp").toMutableList()
//                Log.d("FavDebug", "getFavourites Inter: $favourites")
//                return@executeTransaction favourites
//            }
//        }
//        return emptyList()
}