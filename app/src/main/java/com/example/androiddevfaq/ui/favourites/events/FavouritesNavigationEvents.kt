package com.example.androiddevfaq.ui.favourites.events

sealed class FavouritesNavigationEvents {

    class GoToQuestion(
        val questionID: Int
    ) : FavouritesNavigationEvents()
}