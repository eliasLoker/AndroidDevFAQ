package com.example.androiddevfaq.ui.favourites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevfaq.ui.favourites.interactor.FavouritesInteractor

class FavouritesFactory(
    private val title: String,
    private val favouritesInteractor: FavouritesInteractor
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavouritesViewModel(title, favouritesInteractor) as T
    }
}