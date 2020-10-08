package com.example.androiddevfaq.ui.category.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androiddevfaq.base.BaseAction
import com.example.androiddevfaq.base.BaseViewModel
import com.example.androiddevfaq.base.BaseViewState
import com.example.androiddevfaq.ui.category.adapter.CategoryAdapterListener
import com.example.androiddevfaq.ui.category.event.CategoryNavigationEvents
import com.example.androiddevfaq.ui.category.interactor.CategoryInteractor
import com.example.androiddevfaq.utils.ResultWrapper
import com.example.androiddevfaq.utils.SingleLiveEvent
import com.example.androiddevfaq.utils.mapper.AdapterMapper
import com.example.androiddevfaq.utils.mapper.AdapterMapper.Companion.getRecyclerType
import com.example.androiddevfaq.utils.mapper.AdapterMapper.Companion.toCategoryItemRecycler
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryInteractor: CategoryInteractor
) : BaseViewModel<CategoryViewModel.ViewState, CategoryViewModel.Action>(ViewState()),
    CategoryAdapterListener {

    val initToolbar = MutableLiveData<Any>()

    val categoryNavigationEvents = SingleLiveEvent<CategoryNavigationEvents>()

    private var categoryRecycler = ArrayList<AdapterMapper.CategoryItemRecycler>()

    override fun onActivityCreated(isFirstLoading: Boolean) {
        if (isFirstLoading) {
            initToolbar.value = Any()
            requestCategories(false)
        }
    }

    private fun requestCategories(isSwipeRefresh: Boolean) {
        categoryRecycler.clear()
        sendAction(Action.Loading(isSwipeRefresh))
        viewModelScope.launch {
            when (val fetchCategory = categoryInteractor.getCategories()) {
                is ResultWrapper.Success -> {
                    when (fetchCategory.data.status) {
                        true -> {
                            Log.d("CatNewDeb", "Success: ${categoryRecycler.size}")
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
                            sendAction(Action.Success(categoryRecycler))
                        }
                        false -> {
                            sendAction(Action.Failure(fetchCategory.data.error))
                        }
                    }
                }
                is ResultWrapper.Error -> {
                    sendAction(Action.Error)
                }
            }
        }
    }

    override fun onReduceState(viewAction: Action): ViewState {
        return when (viewAction) {
            is Action.Loading -> when(viewAction.isSwipeRefresh) {
                true -> state.copy(
                    swipeRefreshVisibility = true,
                    sortSpinnerVisibility = false,
                    categoryRecyclerVisibility = false,
                    errorTextViewVisibility = false
                )
                false -> state.copy(
                    progressBarVisibility = true,
                    sortSpinnerVisibility = false,
                    categoryRecyclerVisibility = false,
                    errorTextViewVisibility = false
                )
            }

            is Action.Success -> state.copy(
                progressBarVisibility = false,
                swipeRefreshVisibility = false,
                categoryList = viewAction.categoryList,
                categoryRecyclerVisibility = true,
                sortSpinnerVisibility = true
            )
            is Action.Failure -> state

            is Action.Error -> state

            is Action.SortList -> state.copy(
                categoryList = viewAction.categoryList
            )
        }
    }

    override fun onCategoryClick(position: Int) {
        val event = CategoryNavigationEvents.GoToQuestionList(
            categoryRecycler[position].categoryID,
            categoryRecycler[position].categoryName
        )
        categoryNavigationEvents.value = event
    }

    fun onSwipeRefreshLayout() {
        requestCategories(true)
    }

    fun onSortSpinnerItemSelected(position: Int) {
        when (position) {
            0 -> categoryRecycler.sortBy { it.priority }
            1 -> categoryRecycler.sortByDescending { it.size }
        }
        sendAction(Action.SortList(categoryRecycler))
    }

    data class ViewState(
        val progressBarVisibility: Boolean = true,
        val swipeRefreshVisibility: Boolean = false,
        val categoryRecyclerVisibility: Boolean = false,
        val categoryList: List<AdapterMapper.CategoryItemRecycler> = emptyList(),
        val sortSpinnerVisibility: Boolean = false,
        val errorTextViewVisibility: Boolean = false,
        val errorMessage: String = ""
    ) : BaseViewState

    sealed class Action : BaseAction {

        class Loading(val isSwipeRefresh: Boolean) : Action()

        class SortList(val categoryList: List<AdapterMapper.CategoryItemRecycler>) : Action()

        class Success(val categoryList: List<AdapterMapper.CategoryItemRecycler>) : Action()

        class Failure(val failureMessage: String) : Action()

        object Error : Action()
    }
}