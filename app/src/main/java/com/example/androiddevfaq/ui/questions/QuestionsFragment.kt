package com.example.androiddevfaq.ui.questions

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
import com.example.androiddevfaq.databinding.FragmentQuestionsBinding
import com.example.androiddevfaq.ui.addquestion.AddQuestionFragment
import com.example.androiddevfaq.ui.answer.AnswerFragment
import com.example.androiddevfaq.ui.questions.adapter.QuestionsAdapter
import com.example.androiddevfaq.ui.questions.event.QuestionsNavigationEvents
import com.example.androiddevfaq.ui.questions.interactor.QuestionsInteractor
import com.example.androiddevfaq.ui.questions.viewmodel.QuestionsFactory
import com.example.androiddevfaq.ui.questions.viewmodel.QuestionsViewModel
import com.example.androiddevfaq.utils.AdapterItemDecorator
import com.example.androiddevfaq.utils.navigate
import com.example.androiddevfaq.utils.onItemSelected

class QuestionsFragment(
    layoutID: Int = R.layout.fragment_questions
) : BaseFragment<FragmentQuestionsBinding>(layoutID, FragmentQuestionsBinding::inflate) {

    private lateinit var questionsViewModel: QuestionsViewModel
    private lateinit var questionsAdapter: QuestionsAdapter

    private val stateObserver = Observer<QuestionsViewModel.ViewState> {
        binding.apply {
            questionListProgressBar.isVisible = it.progressBarVisibility
            questionListSwipeRefreshLayout.isRefreshing = it.swipeRefreshVisibility
            sortSpinner.isVisible = it.sortSpinnerVisibility
            questionListRecycler.isVisible = it.questionListAdapterVisibility
            questionListToolbar.toolbar.apply {
                title = it.title
                subtitle = it.subTitle
            }
            emptyTextView.isVisible = it.emptyListTextViewVisibility
            errorTextView.isVisible = it.errorTextViewVisibility
            errorTextView.text = it.errorMessage ?: requireContext().resources.getString(R.string.questions_error)
        }
        questionsAdapter.setData(it.questions)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val categoryID = arguments?.getInt(TAG_FOR_CATEGORY_ID) ?: 0
        val categoryName = arguments?.getString(TAG_FOR_CATEGORY_NAME, "") ?: ""
        val questionListInteractor = QuestionsInteractor(App.getApi())
        val subTitle = requireContext().resources.getString(R.string.question_subtitle)
        val factory = QuestionsFactory(subTitle, categoryID, categoryName, questionListInteractor)

        questionsViewModel = ViewModelProviders.of(this, factory)
            .get(QuestionsViewModel::class.java)

        observe(questionsViewModel.stateLiveData, stateObserver)

        questionsViewModel.onActivityCreated(savedInstanceState == null)

        binding.questionListRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            questionsAdapter = QuestionsAdapter(questionsViewModel)
            adapter = questionsAdapter
            val margin = requireContext().resources.getDimension(R.dimen.default_adapter_padding).toInt()
            addItemDecoration(AdapterItemDecorator(margin))
        }

        val sortOptions = requireContext().resources.getStringArray(R.array.questions_sort_options)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            sortOptions
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        binding.sortSpinner.adapter = adapter

        binding.questionListSwipeRefreshLayout.setOnRefreshListener {
            binding.questionListSwipeRefreshLayout.isRefreshing = true
            questionsViewModel.onSwipeRefreshLayout()
        }

        binding.sortSpinner.onItemSelected {
            questionsViewModel.onSortSpinnerItemSelected(it)
        }

        binding.questionListToolbar.addQuestionMenuImageView.setOnClickListener {
            questionsViewModel.onAddQuestionButtonClicked()
        }

        questionsViewModel.questionsNavigationEvents.observe(viewLifecycleOwner, {
            when (it) {
                is QuestionsNavigationEvents.ToQuestion
                -> goToQuestion(it.questionID)

                is QuestionsNavigationEvents.ToAddQuestion
                -> goToAddQuestion(it.categoryID, it.categoryName)
            }
        })
    }

    private fun goToQuestion(questionID: Int) {
        navigate(
            R.id.action_questionListFragment2_to_questionFragment2,
            AnswerFragment.getBundle(questionID)
        )
    }

    private fun goToAddQuestion(categoryID: Int, categoryName: String) {
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
