package com.example.androiddevfaq.ui.questionlist.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.androiddevfaq.base.BaseAction
import com.example.androiddevfaq.base.BaseViewModel
import com.example.androiddevfaq.base.BaseViewState
import com.example.androiddevfaq.ui.questionlist.adapter.QuestionListListener
import com.example.androiddevfaq.ui.questionlist.event.QuestionsNavigationEvents
import com.example.androiddevfaq.ui.questionlist.interactor.QuestionListInteractor
import com.example.androiddevfaq.utils.ResultWrapper
import com.example.androiddevfaq.utils.SingleLiveEvent
import com.example.androiddevfaq.utils.mapper.AdapterMapper
import com.example.androiddevfaq.utils.mapper.AdapterMapper.Companion.toQuestionItemRecycler
import kotlinx.coroutines.launch

class QuestionListViewModel(
    private val categoryID: Int,
    private val categoryName: String,
    private val interactor: QuestionListInteractor
) : BaseViewModel<QuestionListViewModel.ViewState, QuestionListViewModel.Action>(
    ViewState()
), QuestionListListener {

    private var questions = ArrayList<AdapterMapper.QuestionItemRecycler>()

    val questionsNavigationEvents = SingleLiveEvent<QuestionsNavigationEvents>()

    override fun onActivityCreated(isFirstLoading: Boolean) {
        if (isFirstLoading) {
            sendAction(Action.SetToolbar(categoryName))
            requestQuestions(false)
        }
    }

    private fun requestQuestions(isSwipeRefresh: Boolean) {
        questions.clear()
        sendAction(Action.Loading(isSwipeRefresh))
        viewModelScope.launch {
            when (val fetchQuestions = interactor.getQuestionsList(categoryID)) {
                is ResultWrapper.Success -> {
                    when (fetchQuestions.data.status) {
                        true -> {
                            val questionsTmp = fetchQuestions.data.questionList
                            questionsTmp.forEachIndexed { index, questionListItemSrc ->
                                questions.add(
                                    questionListItemSrc.toQuestionItemRecycler(
                                        AdapterMapper.getRecyclerType(
                                            index
                                        )
                                    )
                                )
                            }
                            sendAction(Action.Success(questions))
                        }

                        false -> {
                            sendAction(Action.Failure(fetchQuestions.data.error))
                        }
                    }
                }

                is ResultWrapper.Error -> {
                    sendAction(Action.Error)
                }
            }
        }
    }

    override fun onClick(position: Int) {
        val event = QuestionsNavigationEvents.ToQuestion(questions[position].questionID)
        questionsNavigationEvents.value = event
    }

    fun onAddQuestionButtonClicked() {
        val event = QuestionsNavigationEvents.ToAddQuestion(categoryID, categoryName)
        questionsNavigationEvents.value = event
    }

    fun onSortSpinnerItemSelected(position: Int) {
        when (position) {
            0 -> questions.sortByDescending { it.rating }
            1 -> questions.sortByDescending { it.timestamp }
        }
        sendAction(Action.Sort(questions))
    }

    fun onSwipeRefreshLayout() {
        requestQuestions(true)
    }

    override fun onReduceState(viewAction: Action): ViewState {
        return when (viewAction) {
            is Action.SetToolbar -> state.copy(
                toolbarTitle = viewAction.toolbarTitle
            )

            is Action.Loading -> {
                when (viewAction.isSwipeRefresh) {
                    true -> state.copy(
                        swipeRefreshVisibility = true,
                        sortSpinnerVisibility = false,
                        questionListAdapterVisibility = false,
                        errorTextViewVisibility = false
                    )
                    false -> state.copy(
                        progressBarVisibility = true,
                        sortSpinnerVisibility = false,
                        questionListAdapterVisibility = false,
                        errorTextViewVisibility = false
                    )
                }
            }
            is Action.Success -> state.copy(
                progressBarVisibility = false,
                swipeRefreshVisibility = false,
                questions = viewAction.questions,
                questionListAdapterVisibility = true,
                sortSpinnerVisibility = true
            )
            is Action.Sort -> state.copy(
                questions = viewAction.sortedQuestions
            )
            is Action.Failure -> state
            is Action.Error -> state
        }
    }

    data class ViewState(
        val toolbarTitle: String = "",
        val progressBarVisibility: Boolean = true,
        val swipeRefreshVisibility: Boolean = false,
        val sortSpinnerVisibility: Boolean = false,
        val questionListAdapterVisibility: Boolean = false,
        val questions: List<AdapterMapper.QuestionItemRecycler> = emptyList(),
        val errorTextViewVisibility: Boolean = false,
        val errorMessage: String = ""
    ) : BaseViewState

    sealed class Action : BaseAction {

        class SetToolbar(val toolbarTitle: String) : Action()

        class Loading(val isSwipeRefresh: Boolean) : Action()

        class Success(val questions: List<AdapterMapper.QuestionItemRecycler>) : Action()

        class Sort(val sortedQuestions: List<AdapterMapper.QuestionItemRecycler>) : Action()

        class Failure(val failureMessage: String) : Action()

        object Error : Action()
    }
}