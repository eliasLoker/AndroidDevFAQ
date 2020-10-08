package com.example.androiddevfaq.ui.test

import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.androiddevfaq.App
import com.example.androiddevfaq.R
import com.example.androiddevfaq.databinding.FragmentTestBinding
import com.example.androiddevfaq.ui.test.interactor.TestInteractor
import com.example.androiddevfaq.ui.test.viewmodel.TestFactory
import com.example.androiddevfaq.ui.test.viewmodel.TestViewModelImpl
import com.example.androiddevfaq.base.BaseFragment
import com.example.androiddevfaq.base.observe

class TestFragment(
    layoutID: Int = R.layout.fragment_test
) : BaseFragment<FragmentTestBinding>(layoutID, FragmentTestBinding::inflate) {

    private lateinit var testViewModel: TestViewModelImpl

    private val stateObserver = Observer<TestViewModelImpl.ViewState> {
        Log.d("AddQuestDebug", "STATE: $it")
        binding.loadingProgressBar.isVisible = it.isLoading
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val interactor = TestInteractor(App.getApi())
        val categoryID = arguments?.getInt(TAG_FOR_CATEGORY_ID, 0) ?: 0
        val categoryName = arguments?.getString(TAG_FOR_CATEGORY_NAME, "") ?: ""
        val factory = TestFactory(categoryID, categoryName, interactor)

        testViewModel =
            ViewModelProviders.of(this, factory).get(TestViewModelImpl::class.java)
        observe(testViewModel.stateLiveData, stateObserver)
        testViewModel.onActivityCreated(savedInstanceState == null)
    }

    companion object {

        private const val TAG_FOR_CATEGORY_ID = "TAG_FOR_CATEGORY_ID"
        private const val TAG_FOR_CATEGORY_NAME = "TAG_FOR_CATEGORY_NAME"

        @JvmStatic
        fun getBundle(categoryID: Int, categoryName: String) = Bundle().apply {
            putInt(TAG_FOR_CATEGORY_ID, categoryID)
            putString(TAG_FOR_CATEGORY_NAME, categoryName)
        }
    }
}