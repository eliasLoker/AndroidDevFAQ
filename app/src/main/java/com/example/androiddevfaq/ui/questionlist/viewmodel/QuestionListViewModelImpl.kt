package com.example.androiddevfaq.ui.questionlist.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevfaq.ui.questionlist.event.GoToAddQuestionEvent
import com.example.androiddevfaq.ui.questionlist.event.GoToQuestionEvent
import com.example.androiddevfaq.ui.questionlist.event.SetQuestionListAdapterEvent
import com.example.androiddevfaq.ui.questionlist.interactor.QuestionListInteractor
import com.example.androiddevfaq.utils.ResultWrapper
import com.example.androiddevfaq.utils.SingleLiveEvent
import com.example.androiddevfaq.utils.mapper.AdapterMapper
import com.example.androiddevfaq.utils.mapper.AdapterMapper.Companion.getRecyclerType
import com.example.androiddevfaq.utils.mapper.AdapterMapper.Companion.toQuestionItemRecycler
import kotlinx.coroutines.launch

class QuestionListViewModelImpl(
    private val categoryID: Int,
    private val categoryName: String,
    private val interactor: QuestionListInteractor
) : ViewModel(), QuestionListViewModel {

    override val initToolbar = MutableLiveData<String>()
    override val progressBarVisibility = SingleLiveEvent<Boolean>()
    override val swipeRefreshVisibility = SingleLiveEvent<Boolean>()
    override val recyclerViewVisibility = SingleLiveEvent<Boolean>()
    override val setQuestionListAdapterEvent = SingleLiveEvent<SetQuestionListAdapterEvent>()
    override val goToQuestionEvent = SingleLiveEvent<GoToQuestionEvent>()
    override val goToAddQuestionEvent = SingleLiveEvent<GoToAddQuestionEvent>()

    private var questionListRecycler = ArrayList<AdapterMapper.QuestionItemRecycler>()

    override fun onActivityCreated() {
        requestQuestionList(false)
        initToolbar.value = categoryName
    }

    override fun onSwipeRefreshLayout() {
        requestQuestionList(true)
    }

    private fun requestQuestionList(isSwipeRefresh: Boolean) {
        recyclerViewVisibility.value = false
        setLoadingVisibility(isSwipeRefresh, true)
        questionListRecycler.clear()
        viewModelScope.launch {
            when (val fetchQuestionList = interactor.getQuestionsList(categoryID)) {
                is ResultWrapper.Success -> {
                    Log.d("QuestListDebuug", "Success: ${fetchQuestionList.data}")
                    setLoadingVisibility(isSwipeRefresh, false)
                    val questions = fetchQuestionList.data.questionList
                    questions.forEachIndexed { index, questionListItemSrc ->
                        questionListRecycler.add(
                            questionListItemSrc.toQuestionItemRecycler(getRecyclerType(index))
                        )
                    }
                    setQuestionListAdapterEvent.value =
                        SetQuestionListAdapterEvent(questionListRecycler)
                    recyclerViewVisibility.value = true

                }

                is ResultWrapper.Error -> {
                    setLoadingVisibility(isSwipeRefresh, false)
                    Log.d("QuestListDebuug", "Error: ${fetchQuestionList.exception}")
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

    override fun onClick(position: Int) {
        goToQuestionEvent.value = GoToQuestionEvent(questionListRecycler[position].questionID)
    }

    override fun onAddQuestionMenuClicked() {
        goToAddQuestionEvent.value = GoToAddQuestionEvent(categoryID, categoryName)
    }
}