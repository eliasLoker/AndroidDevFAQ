package com.example.androiddevfaq.ui.category.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    override val setAdapterEvent = SingleLiveEvent<SetCategoryAdapterEvent>()

    override fun onActivityCreated() {
        viewModelScope.launch {
            when (val fetchCategory = categoryInteractor.getCategories()) {
                is ResultWrapper.Success -> {
                    progressBarVisibility.value = false
                    val category = fetchCategory.data.categoryList
                    val categoryRecycler = ArrayList<AdapterMapper.CategoryRecycler>()
                    category.forEachIndexed { index, categoryItemSrc ->
                        categoryRecycler.add(categoryItemSrc.toCategoryRecycler(categoryItemSrc.getRecyclerType(index)))
                    }
                    setAdapterEvent.value = SetCategoryAdapterEvent(categoryRecycler)
                    Log.d("CatDebuug", "Success: ${fetchCategory.data}")
                }
                is ResultWrapper.Error -> {
                    progressBarVisibility.value = false
                    Log.d("CatDebuug", "Error: ${fetchCategory.exception}")
                }
            }
        }
    }
}