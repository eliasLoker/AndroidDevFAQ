package com.example.androiddevfaq.ui.questionlist

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddevfaq.App
import com.example.androiddevfaq.R
import com.example.androiddevfaq.databinding.FragmentQuestionListBinding
import com.example.androiddevfaq.ui.base.BaseFragment
import com.example.androiddevfaq.ui.addquestion.AddQuestionFragment
import com.example.androiddevfaq.ui.question.QuestionFragment
import com.example.androiddevfaq.ui.questionlist.adapter.QuestionListAdapter
import com.example.androiddevfaq.ui.questionlist.interactor.QuestionListInteractor
import com.example.androiddevfaq.ui.questionlist.viewmodel.QuestionListFactory
import com.example.androiddevfaq.ui.questionlist.viewmodel.QuestionListViewModel
import com.example.androiddevfaq.ui.questionlist.viewmodel.QuestionListViewModelImpl

class QuestionListFragment(
    layoutID: Int = R.layout.fragment_question_list
) : BaseFragment<FragmentQuestionListBinding>(layoutID, FragmentQuestionListBinding::inflate) {

    private lateinit var questionListViewModel: QuestionListViewModel
    private lateinit var questionListAdapter: QuestionListAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val categoryID = arguments?.getInt(TAG_FOR_CATEGORY_ID) ?: 0
        val categoryName = arguments?.getString(TAG_FOR_CATEGORY_NAME, "") ?: ""
//        val api = FakeApiImpl()
        val questionListInteractor = QuestionListInteractor(App.getApi())
        val factory = QuestionListFactory(categoryID, categoryName, questionListInteractor)
        questionListViewModel =
            ViewModelProviders.of(this, factory).get(QuestionListViewModelImpl::class.java)
        questionListViewModel.onActivityCreated()
        initRecycler()
        initListeners()
        initObservers()
    }

    private fun initRecycler() {
        val layoutManager =  LinearLayoutManager(context)
        binding.quesionListRecycler.layoutManager = layoutManager
        questionListAdapter = QuestionListAdapter(questionListViewModel)
        binding.quesionListRecycler.adapter = questionListAdapter
    }

    private fun initListeners() {
        binding.questionListSwipeRefreshLayout.setOnRefreshListener {
            questionListViewModel.onSwipeRefreshLayout()
        }

        binding.questionListToolbar.addQuestionMenuImageView.setOnClickListener {
            questionListViewModel.onAddQuestionMenuClicked()
        }
    }

    private fun initObservers() {

        questionListViewModel.initToolbar.observe(viewLifecycleOwner, {
            binding.questionListToolbar.toolbar.apply {
                title = it
                subtitle = "Вопросы"
            }
        })

        questionListViewModel.progressBarVisibility.observe(viewLifecycleOwner, {
            binding.questionListProgressBar.isVisible = it
        })

        questionListViewModel.recyclerViewVisibility.observe(viewLifecycleOwner, {
            binding.quesionListRecycler.isVisible = it
        })

        questionListViewModel.setQuestionListAdapterEvent.observe(viewLifecycleOwner, {
            questionListAdapter.setData(it.questionList)
        })

        questionListViewModel.swipeRefreshVisibility.observe(viewLifecycleOwner, {
            binding.questionListSwipeRefreshLayout.isRefreshing = it
        })

        questionListViewModel.goToQuestionEvent.observe(viewLifecycleOwner, {
            goToQuestionEvent(it.questionID)
        })

        questionListViewModel.goToAddQuestionEvent.observe(viewLifecycleOwner, {
            goToCreateQuestion(it.categoryID, it.categoryName)
        })
    }

    private fun goToQuestionEvent(questionID: Int) {
        val bundle = QuestionFragment.getBundle(questionID)
        findNavController().navigate(R.id.action_questionListFragment_to_questionFragment, bundle)
    }

    private fun goToCreateQuestion(categoryID: Int, categoryName: String) {
        val bundle = AddQuestionFragment.getBundle(categoryID, categoryName)
        findNavController().navigate(R.id.action_questionListFragment_to_createQuestionFragment, bundle)
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