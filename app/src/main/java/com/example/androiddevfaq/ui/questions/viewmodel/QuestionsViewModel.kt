package com.example.androiddevfaq.ui.questions.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.androiddevfaq.base.BaseAction
import com.example.androiddevfaq.base.BaseViewModel
import com.example.androiddevfaq.base.BaseViewState
import com.example.androiddevfaq.ui.questions.adapter.QuestionsListener
import com.example.androiddevfaq.ui.questions.event.QuestionsNavigationEvents
import com.example.androiddevfaq.ui.questions.interactor.QuestionsInteractor
import com.example.androiddevfaq.ui.questions.model.QuestionsItem
import com.example.androiddevfaq.ui.questions.model.QuestionsItem.Companion.toQuestionItemRecycler
import com.example.androiddevfaq.utils.ResultWrapper
import com.example.androiddevfaq.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class QuestionsViewModel(
    private val subTitle: String,
    private val categoryID: Int,
    private val categoryName: String,
    private val interactor: QuestionsInteractor
) : BaseViewModel<QuestionsViewModel.ViewState, QuestionsViewModel.Action>(
    ViewState()
), QuestionsListener {

    private var questions = ArrayList<QuestionsItem.QuestionsItemDst>()

    val questionsNavigationEvents = SingleLiveEvent<QuestionsNavigationEvents>()

    override fun onActivityCreated(isFirstLoading: Boolean) {
        if (isFirstLoading) {
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
                            val questionsTmp = fetchQuestions.data.questionList ?: emptyList()
                            questionsTmp.forEachIndexed { index, questionListItemSrc ->
                                questions.add(
                                    questionListItemSrc.toQuestionItemRecycler(
                                        QuestionsItem.getRecyclerType(index),
                                        QuestionsItem.parseTimestampToDate(
                                            questionListItemSrc.timestamp ?: 0
                                        )
                                    )
                                )
                            }
                            when (questions.isEmpty()) {
                                true -> sendAction(Action.SuccessEmpty)
                                false -> sendAction(Action.SuccessNotEmpty(questions))
                            }
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
        val questionID = questions[position].questionID ?: return
        val event = QuestionsNavigationEvents.ToQuestion(questionID)
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
            is Action.Loading -> {
                when (viewAction.isSwipeRefresh) {
                    true -> state.copy(
                        title = categoryName,
                        subTitle = subTitle,
                        swipeRefreshVisibility = true,
                        sortSpinnerVisibility = false,
                        questionListAdapterVisibility = false,
                        errorTextViewVisibility = false
                    )
                    false -> state.copy(
                        title = categoryName,
                        subTitle = subTitle,
                        progressBarVisibility = true,
                        sortSpinnerVisibility = false,
                        questionListAdapterVisibility = false,
                        errorTextViewVisibility = false
                    )
                }
            }
            is Action.SuccessNotEmpty -> state.copy(
                progressBarVisibility = false,
                swipeRefreshVisibility = false,
                questions = viewAction.questions,
                questionListAdapterVisibility = true,
                sortSpinnerVisibility = true,
                emptyListTextViewVisibility = false
            )
            is Action.SuccessEmpty -> state.copy(
                progressBarVisibility = false,
                swipeRefreshVisibility = false,
                questionListAdapterVisibility = false,
                sortSpinnerVisibility = false,
                emptyListTextViewVisibility = true
            )
            is Action.Sort -> state.copy(
                questions = viewAction.sortedQuestions
            )
            is Action.Failure -> state.copy(
                progressBarVisibility = false,
                swipeRefreshVisibility = false,
                sortSpinnerVisibility = false,
                questionListAdapterVisibility = false,
                emptyListTextViewVisibility = false,
                errorTextViewVisibility = true,
                errorMessage = viewAction.failureMessage
            )
            is Action.Error -> state.copy(
                progressBarVisibility = false,
                swipeRefreshVisibility = false,
                sortSpinnerVisibility = false,
                questionListAdapterVisibility = false,
                emptyListTextViewVisibility = false,
                errorTextViewVisibility = true,
                errorMessage = null
            )
        }
    }

    data class ViewState(
        val title: String = "",
        val subTitle: String = "",
        val progressBarVisibility: Boolean = true,
        val swipeRefreshVisibility: Boolean = false,
        val sortSpinnerVisibility: Boolean = false,
        val questionListAdapterVisibility: Boolean = false,
        val questions: List<QuestionsItem.QuestionsItemDst> = emptyList(),
        val emptyListTextViewVisibility: Boolean = false,
        val errorTextViewVisibility: Boolean = false,
        val errorMessage: String? = ""
    ) : BaseViewState

    sealed class Action : BaseAction {

        class Loading(val isSwipeRefresh: Boolean) : Action()

        class SuccessNotEmpty(val questions: List<QuestionsItem.QuestionsItemDst>) : Action()

        object SuccessEmpty : Action()

        class Sort(val sortedQuestions: List<QuestionsItem.QuestionsItemDst>) : Action()

        class Failure(val failureMessage: String?) : Action()

        object Error : Action()
    }
}