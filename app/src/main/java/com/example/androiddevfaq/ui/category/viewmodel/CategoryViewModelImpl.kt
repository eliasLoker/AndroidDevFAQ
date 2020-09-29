package com.example.androiddevfaq.ui.category.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevfaq.ui.category.event.GoToNextFragmentEvents
import com.example.androiddevfaq.ui.category.event.SetCategoryAdapterEvent
import com.example.androiddevfaq.ui.category.interactor.CategoryInteractor
import com.example.androiddevfaq.utils.ResultWrapper
import com.example.androiddevfaq.utils.SingleLiveEvent
import com.example.androiddevfaq.utils.mapper.AdapterMapper
import com.example.androiddevfaq.utils.mapper.AdapterMapper.Companion.getRecyclerType
import com.example.androiddevfaq.utils.mapper.AdapterMapper.Companion.toCategoryItemRecycler
import kotlinx.coroutines.launch

class CategoryViewModelImpl(
    private val categoryInteractor: CategoryInteractor
) : ViewModel(), CategoryViewModel {

    override val initToolbar = MutableLiveData<Any>()
    override val progressBarVisibility = SingleLiveEvent<Boolean>()
    override val recyclerViewVisibility = SingleLiveEvent<Boolean>()
    override val setAdapterEvent = SingleLiveEvent<SetCategoryAdapterEvent>()
    override val swipeRefreshVisibility = SingleLiveEvent<Boolean>()
    override val initSortSpinner = SingleLiveEvent<Any>()
    override val goToNextFragmentEvents = SingleLiveEvent<GoToNextFragmentEvents>()
    override val sortSpinnerVisibility = SingleLiveEvent<Boolean>()

    private var categoryRecycler = ArrayList<AdapterMapper.CategoryItemRecycler>()

    override fun onActivityCreated() {
        requestCategories(false)
        initToolbar.value = Any()
    }

    override fun onSwipeRefreshLayout() {
        requestCategories(true)
    }

    override fun onCategoryClick(position: Int) {
        goToNextFragmentEvents.value =
            GoToNextFragmentEvents.QuestionList(
                categoryRecycler[position].categoryID,
                categoryRecycler[position].categoryName
            )
    }

    override fun onSortSpinnerItemSelected(position: Int) {
        Log.d("CatDebuug", "onSortSpinnerItemSelected: pos: $position")
        Log.d("CatDebuug", "before: $categoryRecycler")
        when (position) {
            0 -> categoryRecycler.sortBy { it.priority }
            1 -> categoryRecycler.sortByDescending { it.size }
        }
        setAdapterEvent.value = SetCategoryAdapterEvent(categoryRecycler)
        Log.d("CatDebuug", "after: $categoryRecycler")
    }

    private fun requestCategories(isSwipeRefresh: Boolean) {
        initSortSpinner.value = Any()
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
                            categoryItemSrc.toCategoryItemRecycler(
                                getRecyclerType(
                                    index
                                )
                            )
                        )
                    }
                    setAdapterEvent.value = SetCategoryAdapterEvent(categoryRecycler)
                    recyclerViewVisibility.value = true
                    sortSpinnerVisibility.value = true
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
        sortSpinnerVisibility.value = !isLoading
        when (isSwipeRefresh) {
            true -> swipeRefreshVisibility.value = isLoading
            false -> progressBarVisibility.value = isLoading
        }
    }
}