package com.example.androiddevfaq.ui.favourites

import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddevfaq.R
import com.example.androiddevfaq.databinding.FragmentFavouritesBinding
import com.example.androiddevfaq.base.BaseFragment
import com.example.androiddevfaq.base.observe
import com.example.androiddevfaq.ui.favourites.adapter.FavouritesAdapter
import com.example.androiddevfaq.ui.favourites.events.FavouritesNavigationEvents
import com.example.androiddevfaq.ui.favourites.interactor.FavouritesInteractor
import com.example.androiddevfaq.ui.favourites.viewmodel.FavouritesFactory
import com.example.androiddevfaq.ui.favourites.viewmodel.FavouritesViewModel
import com.example.androiddevfaq.ui.answer.AnswerFragment
import com.example.androiddevfaq.utils.AdapterItemDecorator
import com.example.androiddevfaq.utils.navigate
import io.realm.Realm

class FavouritesFragment(
    layoutID: Int = R.layout.fragment_favourites
) : BaseFragment<FragmentFavouritesBinding>(layoutID, FragmentFavouritesBinding::inflate) {

    private lateinit var favouritesViewModel: FavouritesViewModel
    private lateinit var favouritesAdapter: FavouritesAdapter

    private val stateObserver = Observer<FavouritesViewModel.ViewState> {
        binding.apply {
            progressBar.isVisible = it.progressBarVisibility
            recycler.isVisible = it.recyclerVisibility
            toolbar.toolbar.apply {
                title = it.title
            }
            emptyTextView.isVisible = it.emptyListTextViewVisibility
//            favouritesAdapter.setData(it.list)
            favouritesAdapter.data = it.list
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val title = requireContext().resources.getString(R.string.favourites_title)
        val realm = Realm.getDefaultInstance()
        val interactor = FavouritesInteractor(realm)
        val factory = FavouritesFactory(title, interactor)
        favouritesViewModel = ViewModelProviders.of(this, factory)
            .get(FavouritesViewModel::class.java)
        observe(favouritesViewModel.stateLiveData, stateObserver)
        favouritesViewModel.onActivityCreated(savedInstanceState == null)

        binding.recycler.apply {
            favouritesAdapter = FavouritesAdapter(favouritesViewModel)
            layoutManager = LinearLayoutManager(context)
            adapter = favouritesAdapter
            val margin = requireContext().resources.getDimension(R.dimen.default_adapter_padding).toInt()
            addItemDecoration(AdapterItemDecorator(margin))
        }

        favouritesViewModel.navigationEvents.observe(viewLifecycleOwner, {
            when(it) {
                is FavouritesNavigationEvents.GoToQuestion
                -> navigate(
                    R.id.action_favouritesFragment_to_questionFragment2,
                    AnswerFragment.getBundleFromDatabase(it.questionID)
                )
            }
        })
    }
}