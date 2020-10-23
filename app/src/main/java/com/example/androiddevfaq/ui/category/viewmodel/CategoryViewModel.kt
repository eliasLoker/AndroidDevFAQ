package com.example.androiddevfaq.ui.category.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.androiddevfaq.base.BaseAction
import com.example.androiddevfaq.base.BaseViewModel
import com.example.androiddevfaq.base.BaseViewState
import com.example.androiddevfaq.ui.category.adapter.CategoryAdapterListener
import com.example.androiddevfaq.ui.category.event.CategoryNavigationEvents
import com.example.androiddevfaq.ui.category.interactor.CategoryInteractor
import com.example.androiddevfaq.ui.category.model.CategoryItem
import com.example.androiddevfaq.ui.category.model.CategoryItem.Companion.toCategoryItemDst
import com.example.androiddevfaq.utils.ResultWrapper
import com.example.androiddevfaq.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val title: String,
    private val subTitle: String,
    private val categoryInteractor: CategoryInteractor
) : BaseViewModel<CategoryViewModel.ViewState, CategoryViewModel.Action>(ViewState()),
    CategoryAdapterListener {

    val categoryNavigationEvents = SingleLiveEvent<CategoryNavigationEvents>()

    private var categoryRecycler = ArrayList<CategoryItem.CategoryItemDst>()

    private var sortType = 0
    //0 - default (priority)
    //1 -size

    override fun onActivityCreated(isFirstLoading: Boolean) {
        if (isFirstLoading) {
            requestCategories(false)
        }
    }

    private fun requestCategories(isSwipeRefresh: Boolean) {
        categoryRecycler.clear()
        sendAction(Action.Loading(isSwipeRefresh))
        viewModelScope.launch {
            when (val fetchCategory = categoryInteractor.getCategories()) {
                is ResultWrapper.Success -> {
                    when (fetchCategory.data.status ?: false) {
                        true -> {
                            val category = fetchCategory.data.categoryList ?: emptyList()
                            Log.d("CatDebug", "category: $category")
                            category.forEachIndexed { index, categoryItemSrc ->
                                categoryRecycler.add(
                                    categoryItemSrc.toCategoryItemDst(
                                        CategoryItem.getRecyclerType(
                                            index
                                        )
                                    )
                                )
                            }
                            Log.d("CatDebug", "WOS: $categoryRecycler")
                            sortList()
                            Log.d("CatDebug", "SORT: $categoryRecycler")
                            when (categoryRecycler.isEmpty()) {
                                true -> sendAction(Action.SuccessEmpty)
                                false -> sendAction(Action.SuccessNotEmpty(categoryRecycler))
                            }
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

    private fun sortList() {
        when (sortType) {
            0 -> categoryRecycler.sortBy { it.priority }
            1 -> categoryRecycler.sortByDescending { it.size }
        }
    }

    override fun onReduceState(viewAction: Action): ViewState {
        return when (viewAction) {

            is Action.Loading -> when (viewAction.isSwipeRefresh) {
                true -> state.copy(
                    title = title,
                    subTitle = subTitle,
                    swipeRefreshVisibility = true,
                    sortSpinnerVisibility = false,
                    categoryRecyclerVisibility = false,
                    emptyListTextViewVisibility = false,
                    errorTextViewVisibility = false
                )
                false -> state.copy(
                    title = title,
                    subTitle = subTitle,
                    progressBarVisibility = true,
                    sortSpinnerVisibility = false,
                    categoryRecyclerVisibility = false,
                    emptyListTextViewVisibility = false,
                    errorTextViewVisibility = false
                )
            }

            is Action.SuccessEmpty -> state.copy(
                progressBarVisibility = false,
                swipeRefreshVisibility = false,
                categoryRecyclerVisibility = false,
                emptyListTextViewVisibility = true,
                sortSpinnerVisibility = false
            )

            is Action.SuccessNotEmpty -> state.copy(
                progressBarVisibility = false,
                swipeRefreshVisibility = false,
                categoryList = viewAction.categoryList,
                categoryRecyclerVisibility = true,
                emptyListTextViewVisibility = false,
                sortSpinnerVisibility = true
            )

            is Action.SortList -> state.copy(
                categoryList = viewAction.categoryList
            )

            is Action.Failure -> state.copy(
                progressBarVisibility = false,
                swipeRefreshVisibility = false,
                categoryRecyclerVisibility = false,
                emptyListTextViewVisibility = false,
                sortSpinnerVisibility = false,
                errorTextViewVisibility = true,
                errorMessage = viewAction.failureMessage
            )

            is Action.Error -> state.copy(
                progressBarVisibility = false,
                swipeRefreshVisibility = false,
                categoryRecyclerVisibility = false,
                emptyListTextViewVisibility = false,
                sortSpinnerVisibility = false,
                errorTextViewVisibility = true,
                errorMessage = null
            )
        }
    }

    override fun onCategoryClick(position: Int) {
        val id = categoryRecycler[position].categoryID ?: return
        val name = categoryRecycler[position].categoryName ?: return
        val event = CategoryNavigationEvents.GoToQuestionList(
            id,
            name
        )
        categoryNavigationEvents.value = event
    }

    fun onSwipeRefreshLayout() {
        requestCategories(true)
    }

    fun onSortSpinnerItemSelected(position: Int) {
        sortType = position
        sortList()
//        when (position) {
//            0 -> categoryRecycler.sortBy { it.priority }
//            1 -> categoryRecycler.sortByDescending { it.size }
//        }
        sendAction(Action.SortList(categoryRecycler))
    }

    data class ViewState(
        val title: String = "",
        val subTitle: String = "",
        val progressBarVisibility: Boolean = true,
        val swipeRefreshVisibility: Boolean = false,
        val categoryRecyclerVisibility: Boolean = false,
        val categoryList: List<CategoryItem.CategoryItemDst> = emptyList(),
        val emptyListTextViewVisibility: Boolean = false,
        val sortSpinnerVisibility: Boolean = false,
        val errorTextViewVisibility: Boolean = false,
        val errorMessage: String? = ""
    ) : BaseViewState

    sealed class Action : BaseAction {

        class Loading(
            val isSwipeRefresh: Boolean
        ) : Action()

        class SuccessNotEmpty(
            val categoryList: List<CategoryItem.CategoryItemDst>
        ) : Action()

        object SuccessEmpty : Action()

        class SortList(
            val categoryList: List<CategoryItem.CategoryItemDst>
        ) : Action()

        class Failure(
            val failureMessage: String?
        ) : Action()

        object Error : Action()
    }
}