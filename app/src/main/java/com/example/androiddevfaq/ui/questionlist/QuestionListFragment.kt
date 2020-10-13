package com.example.androiddevfaq.ui.questionlist

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddevfaq.App
import com.example.androiddevfaq.R
import com.example.androiddevfaq.base.BaseFragment
import com.example.androiddevfaq.base.observe
import com.example.androiddevfaq.databinding.FragmentQuestionListBinding
import com.example.androiddevfaq.ui.addquestion.AddQuestionFragment
import com.example.androiddevfaq.ui.question.QuestionFragment
import com.example.androiddevfaq.ui.questionlist.adapter.QuestionListAdapter
import com.example.androiddevfaq.ui.questionlist.event.QuestionsNavigationEvents
import com.example.androiddevfaq.ui.questionlist.interactor.QuestionListInteractor
import com.example.androiddevfaq.ui.questionlist.viewmodel.QuestionListFactory
import com.example.androiddevfaq.ui.questionlist.viewmodel.QuestionListViewModel
import com.example.androiddevfaq.utils.navigate
import com.example.androiddevfaq.utils.onItemSelected

class QuestionListFragment(
    layoutID: Int = R.layout.fragment_question_list
) : BaseFragment<FragmentQuestionListBinding>(layoutID, FragmentQuestionListBinding::inflate) {

    private lateinit var questionListViewModel: QuestionListViewModel
    private lateinit var questionListAdapter: QuestionListAdapter

    private val stateObserver = Observer<QuestionListViewModel.ViewState> {
        binding.questionListProgressBar.isVisible = it.progressBarVisibility
        binding.questionListSwipeRefreshLayout.isRefreshing = it.swipeRefreshVisibility
        binding.sortSpinner.isVisible = it.sortSpinnerVisibility
        binding.questionListRecycler.isVisible = true
        binding.questionListToolbar.toolbar.apply {
            title = it.toolbarTitle
            subtitle = "Вопросы"
        }
        questionListAdapter.setData(it.questions)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val categoryID = arguments?.getInt(TAG_FOR_CATEGORY_ID) ?: 0
        val categoryName = arguments?.getString(TAG_FOR_CATEGORY_NAME, "") ?: ""
        val questionListInteractor = QuestionListInteractor(App.getApi())
        val factory = QuestionListFactory(categoryID, categoryName, questionListInteractor)

        questionListViewModel = ViewModelProviders.of(this, factory)
            .get(QuestionListViewModel::class.java)

        observe(questionListViewModel.stateLiveData, stateObserver)

        questionListViewModel.onActivityCreated(savedInstanceState == null)

        val layoutManager = LinearLayoutManager(context)
        binding.questionListRecycler.layoutManager = layoutManager
        questionListAdapter = QuestionListAdapter(questionListViewModel)
        binding.questionListRecycler.adapter = questionListAdapter

        val sortOptions = arrayOf("По рейтингу", "По дате")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            sortOptions
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        binding.sortSpinner.adapter = adapter

        binding.questionListSwipeRefreshLayout.setOnRefreshListener {
            binding.questionListSwipeRefreshLayout.isRefreshing = true
            questionListViewModel.onSwipeRefreshLayout()
        }

        binding.sortSpinner.onItemSelected {
            questionListViewModel.onSortSpinnerItemSelected(it)
        }

        binding.questionListToolbar.addQuestionMenuImageView.setOnClickListener {
            questionListViewModel.onAddQuestionButtonClicked()
        }

        questionListViewModel.questionsNavigationEvents.observe(viewLifecycleOwner, {
            when(it) {
                is QuestionsNavigationEvents.ToQuestion
                -> goToQuestion(it.questionID)

                is QuestionsNavigationEvents.ToAddQuestion
                -> goToAddQuestion(it.categoryID, it.categoryName)
            }
        })
    }

    private fun goToQuestion(questionID: Int) {
//        val bundle = QuestionFragmentOld.getBundle(questionID)
//        findNavController().navigate(R.id.action_questionListFragment2_to_questionFragment, bundle)
        navigate(
            R.id.action_questionListFragment2_to_questionFragment2,
            QuestionFragment.getBundle(questionID)
        )
    }

    private fun goToAddQuestion(categoryID: Int, categoryName: String) {
//        val bundle = AddQuestionFragmentOld.getBundle(categoryID, categoryName)
//        findNavController().navigate(R.id.action_questionListFragment2_to_createQuestionFragment, bundle)
        navigate(
            R.id.action_questionListFragment2_to_addQuestionFragment2,
            AddQuestionFragment.getBundle(categoryID, categoryName)
        )
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
