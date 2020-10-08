package com.example.androiddevfaq.ui.test.viewmodel

import com.example.androiddevfaq.ui.test.interactor.TestInteractor
import com.example.androiddevfaq.base.BaseAction
import com.example.androiddevfaq.base.BaseViewModel
import com.example.androiddevfaq.base.BaseViewState

class TestViewModelImpl(
    private val categoryID: Int,
    private val categoryName: String,
    private val testInteractor: TestInteractor
) : BaseViewModel<TestViewModelImpl.ViewState, TestViewModelImpl.Action>(ViewState()) {

    override fun onActivityCreated(isFirstLoading: Boolean) {
        sendAction(Action.LoadSuccess("sadfs"))
    }

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        is Action.LoadSuccess -> state.copy(
            isLoading = false
//            ,
//            isError = false
        )

        is Action.LoadFailure -> state.copy(
            isLoading = false,
            isError = true
        )
    }


    data class ViewState(
        val isLoading: Boolean = true,
        val isError: Boolean = false
    ) : BaseViewState

    sealed class Action : BaseAction {
        class LoadSuccess(val message: String) : Action()

        object LoadFailure : Action()
    }
}