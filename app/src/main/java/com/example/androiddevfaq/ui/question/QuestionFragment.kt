package com.example.androiddevfaq.ui.question

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.androiddevfaq.App
import com.example.androiddevfaq.R
import com.example.androiddevfaq.base.BaseFragment
import com.example.androiddevfaq.base.observe
import com.example.androiddevfaq.databinding.FragmentQuestionBinding
import com.example.androiddevfaq.ui.question.interactor.QuestionInteractor
import com.example.androiddevfaq.ui.question.viewmodel.QuestionFactory
import com.example.androiddevfaq.ui.question.viewmodel.QuestionViewModel
import com.example.androiddevfaq.ui.questionlist.viewmodel.QuestionListViewModel
import io.realm.Realm

class QuestionFragment(
    layoutID: Int = R.layout.fragment_question
) : BaseFragment<FragmentQuestionBinding>(layoutID, FragmentQuestionBinding::inflate) {

    private lateinit var questionViewModel: QuestionViewModel

    private val stateObserver = Observer<QuestionViewModel.ViewState> {
        binding.mainProgressBar.isVisible = it.progressBarVisibility
        binding.titleQuestionTextView.text = it.questionText
        binding.answer.text = it.answerText
        binding.titleQuestionTextView.isVisible = it.questionVisibility
        binding.answer.isVisible = it.answerVisibility
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val questionID = arguments?.getInt(TAG_FOR_QUESTION_ID, 0) ?: 0

        val isFromDatabase = arguments?.getBoolean(TAG_FOR_FROM_DATABASE, false) ?: false

        val realm = Realm.getDefaultInstance()
        val interactor = QuestionInteractor(App.getApi(), realm)
        val factory = QuestionFactory(isFromDatabase, questionID, interactor)
        questionViewModel = ViewModelProviders.of(this, factory)
            .get(QuestionViewModel::class.java)
        observe(questionViewModel.stateLiveData, stateObserver)
        questionViewModel.onActivityCreated(savedInstanceState == null)

        binding.addFavouritesButton.setOnClickListener {
            questionViewModel.onAddQuestionButtonClicked()
        }
    }

    companion object {

        private const val TAG_FOR_QUESTION_ID = "TAG_FOR_QUESTION_ID"
        private const val TAG_FOR_FROM_DATABASE = "TAG_FOR_FROM_DATABASE"

        @JvmStatic
        fun getBundle(questionID: Int) = Bundle().apply {
            putInt(TAG_FOR_QUESTION_ID, questionID)
        }

        @JvmStatic
        fun getBundleFromDatabase(
            questionID: Int
        ) = Bundle().apply {
            putBoolean(TAG_FOR_FROM_DATABASE, true)
            putInt(TAG_FOR_QUESTION_ID, questionID)
        }
    }
}