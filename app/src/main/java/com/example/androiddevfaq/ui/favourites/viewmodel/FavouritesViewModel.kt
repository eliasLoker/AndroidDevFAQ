package com.example.androiddevfaq.ui.favourites.viewmodel

import android.util.Log
import com.example.androiddevfaq.base.BaseAction
import com.example.androiddevfaq.base.BaseViewModel
import com.example.androiddevfaq.base.BaseViewState
import com.example.androiddevfaq.ui.favourites.adapter.FavouritesAdapterListener
import com.example.androiddevfaq.ui.favourites.events.FavouritesNavigationEvents
import com.example.androiddevfaq.ui.favourites.interactor.FavouritesInteractor
import com.example.androiddevfaq.ui.favourites.model.FavouriteItem
import com.example.androiddevfaq.ui.favourites.model.FavouriteItem.Companion.toFavouriteItemDst
import com.example.androiddevfaq.utils.SingleLiveEvent

class FavouritesViewModel(
    private val title: String,
    private val favouritesInteractor: FavouritesInteractor
) : BaseViewModel<FavouritesViewModel.ViewState, FavouritesViewModel.Action>(ViewState()),
    FavouritesAdapterListener {

    val navigationEvents = SingleLiveEvent<FavouritesNavigationEvents>()

    private val favouritesItems = ArrayList<FavouriteItem.FavouriteItemDst>()

    override fun onActivityCreated(isFirstLoading: Boolean) {
        fetchFavourites()
    }

    private fun fetchFavourites() {
        sendAction(Action.Loading)
        favouritesItems.clear()
        val favourites = favouritesInteractor.getFavourites()
        favourites.forEachIndexed { index, favouriteQuestion ->
            favouritesItems.add(favouriteQuestion.toFavouriteItemDst(FavouriteItem.getRecyclerType(index)))
        }
        when(favouritesItems.isEmpty()) {
            true -> sendAction(Action.SuccessEmpty)
            false -> sendAction(Action.SuccessNotEmpty(favouritesItems))
        }
    }

    override fun onClick(position: Int) {
        navigationEvents.value = FavouritesNavigationEvents.GoToQuestion(favouritesItems[position].questionID)
    }

    override fun onReduceState(viewAction: Action) = when (viewAction) {
        is Action.Loading -> state.copy(
            title = title,
            progressBarVisibility = true,
            recyclerVisibility = false
        )
        is Action.SuccessNotEmpty -> state.copy(
            progressBarVisibility = false,
            recyclerVisibility = true,
            list = viewAction.list
        )

        is Action.SuccessEmpty -> state.copy(
            progressBarVisibility = false,
            recyclerVisibility = false,
            emptyListTextViewVisibility = true
        )
    }

    data class ViewState(
        val title: String = "",
        val progressBarVisibility: Boolean = true,
        val recyclerVisibility: Boolean = false,
        val list: List<FavouriteItem.FavouriteItemDst> = emptyList(),
        val emptyListTextViewVisibility: Boolean = false
    ) : BaseViewState

    sealed class Action : BaseAction {

        object Loading : Action()

        class SuccessNotEmpty(
            val list: List<FavouriteItem.FavouriteItemDst>
        ) : Action()

        object SuccessEmpty : Action()
    }
}