package com.example.androiddevfaq.ui.category.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevfaq.ui.category.event.GoToNextFragmentEvents
import com.example.androiddevfaq.ui.category.event.SetCategoryAdapterEvent
import com.example.androiddevfaq.ui.category.interactor.CategoryInteractor
import com.example.androiddevfaq.utils.ResultWrapper
import com.example.androiddevfaq.utils.SingleLiveEvent
import com.example.androiddevfaq.utils.mapper.AdapterMapper
import com.example.androiddevfaq.utils.mapper.AdapterMapper.Companion.getRecyclerType
import com.example.androiddevfaq.utils.mapper.AdapterMapper.Companion.toCategoryRecycler
import kotlinx.coroutines.launch

class CategoryViewModelImpl(
    private val categoryInteractor: CategoryInteractor
) : ViewModel(), CategoryViewModel {

    override val progressBarVisibility = SingleLiveEvent<Boolean>()
    override val recyclerViewVisibility = SingleLiveEvent<Boolean>()
    override val setAdapterEvent = SingleLiveEvent<SetCategoryAdapterEvent>()
    override val swipeRefreshVisibility = SingleLiveEvent<Boolean>()
    override val goToNextFragmentEvents = SingleLiveEvent<GoToNextFragmentEvents>()

    var categoryRecycler = ArrayList<AdapterMapper.CategoryRecycler>()

    override fun onActivityCreated() {
        requestCategories(false)
    }

    override fun onSwipeRefreshLayout() {
        requestCategories(true)
    }

    override fun onCategoryClick(position: Int) {
//        Log.d("onCategoryClick", "pos: $position")
        goToNextFragmentEvents.value =
            GoToNextFragmentEvents.QuestionList(categoryRecycler[position].categoryID)
    }

    private fun requestCategories(isSwipeRefresh: Boolean) {
        recyclerViewVisibility.value = false
        setLoadingVisibility(isSwipeRefresh, true)
        categoryRecycler.clear()
        viewModelScope.launch {
            when (val fetchCategory = categoryInteractor.getCategories()) {
                is ResultWrapper.Success -> {
                    setLoadingVisibility(isSwipeRefresh, false)
                    val category = fetchCategory.data.categoryList
                    category.forEachIndexed { index, categoryItemSrc ->
                        categoryRecycler.add(
                            categoryItemSrc.toCategoryRecycler(
                                getRecyclerType(
                                    index
                                )
                            )
                        )
                    }
                    setAdapterEvent.value = SetCategoryAdapterEvent(categoryRecycler)
                    recyclerViewVisibility.value = true
                    Log.d("CatDebuug", "Success: ${fetchCategory.data}")
                }
                is ResultWrapper.Error -> {
                    setLoadingVisibility(isSwipeRefresh, false)
                    Log.d("CatDebuug", "Error: ${fetchCategory.exception}")
                }
            }
        }
    }

    private fun setLoadingVisibility(isSwipeRefresh: Boolean, isLoading: Boolean) {
        when (isSwipeRefresh) {
            true -> swipeRefreshVisibility.value = isLoading
            false -> progressBarVisibility.value = isLoading
        }
    }
}