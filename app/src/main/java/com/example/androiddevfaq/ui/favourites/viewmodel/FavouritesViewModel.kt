package com.example.androiddevfaq.ui.favourites.viewmodel

import com.example.androiddevfaq.base.BaseAction
import com.example.androiddevfaq.base.BaseViewModel
import com.example.androiddevfaq.base.BaseViewState
import com.example.androiddevfaq.ui.favourites.interactor.FavouritesInteractor
import com.example.androiddevfaq.utils.mapper.AdapterMapper
import com.example.androiddevfaq.utils.mapper.AdapterMapper.Companion.getRecyclerType
import com.example.androiddevfaq.utils.mapper.AdapterMapper.Companion.toFavouriteItemRecycler

class FavouritesViewModel(
    private val title: String,
    private val favouritesInteractor: FavouritesInteractor
) : BaseViewModel<FavouritesViewModel.ViewState, FavouritesViewModel.Action>(ViewState()) {

    private val favouritesItems = ArrayList<AdapterMapper.FavouriteItemRecycler>()

    override fun onActivityCreated(isFirstLoading: Boolean) {
        sendAction(Action.SetToolbar(title))
//        sendAction(Action.SuccessNotEmpty(emptyList()))
        getFavourites()
    }

    private fun getFavourites() {
        val favourites = favouritesInteractor.getFavourites()
        favourites.forEachIndexed { index, favouriteQuestion ->
            favouritesItems.add(favouriteQuestion.toFavouriteItemRecycler(getRecyclerType(index)))
        }
        sendAction(Action.SuccessNotEmpty(favouritesItems))
    }

    override fun onReduceState(viewAction: Action) = when (viewAction) {
        is Action.SetToolbar -> state.copy(
            title = viewAction.title
        )
        is Action.Loading -> state.copy(
            progressBarVisibility = true,
            recyclerVisibility = false
        )
        is Action.SuccessNotEmpty -> state.copy(
            progressBarVisibility = false,
            recyclerVisibility = true,
            list = viewAction.list
        )
    }

    data class ViewState(
        val title: String = "",
        val progressBarVisibility: Boolean = true,
        val recyclerVisibility: Boolean = false,
        val list: List<AdapterMapper.FavouriteItemRecycler> = emptyList()
    ) : BaseViewState

    sealed class Action : BaseAction {

        class SetToolbar(
            val title: String
        ) : Action()

        object Loading : Action()

        class SuccessNotEmpty(
            val list: List<AdapterMapper.FavouriteItemRecycler>
        ) : Action()
    }
}