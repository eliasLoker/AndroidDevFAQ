package com.example.androiddevfaq.ui.favourites

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddevfaq.R
import com.example.androiddevfaq.databinding.FragmentFavouritesBinding
import com.example.androiddevfaq.base.BaseFragment
import com.example.androiddevfaq.base.observe
import com.example.androiddevfaq.ui.favourites.adapter.FavouriteAdapter
import com.example.androiddevfaq.ui.favourites.interactor.FavouritesInteractor
import com.example.androiddevfaq.ui.favourites.viewmodel.FavouritesFactory
import com.example.androiddevfaq.ui.favourites.viewmodel.FavouritesViewModel
import io.realm.Realm

class FavouritesFragment(
    layoutID: Int = R.layout.fragment_favourites
) : BaseFragment<FragmentFavouritesBinding>(layoutID, FragmentFavouritesBinding::inflate) {

    private lateinit var favouritesViewModel: FavouritesViewModel
    private lateinit var favouritesAdapter: FavouriteAdapter

    private val stateObserver = Observer<FavouritesViewModel.ViewState> {
        binding.apply {
            progressBar.isVisible = it.progressBarVisibility
            recycler.isVisible = it.recyclerVisibility
            toolbar.toolbar.apply {
                title = it.title
            }
            favouritesAdapter.setData(it.list)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val title = "Избранное"
        val realm = Realm.getDefaultInstance()
        val interactor = FavouritesInteractor(realm)
        val factory = FavouritesFactory(title, interactor)
        favouritesViewModel = ViewModelProviders.of(this, factory)
            .get(FavouritesViewModel::class.java)
        observe(favouritesViewModel.stateLiveData, stateObserver)
        favouritesViewModel.onActivityCreated(savedInstanceState == null)

        binding.recycler.apply {
            favouritesAdapter = FavouriteAdapter()
            layoutManager = LinearLayoutManager(context)
            adapter = favouritesAdapter
        }
    }
}