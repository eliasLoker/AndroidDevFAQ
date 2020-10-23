package com.example.androiddevfaq.ui.answer

import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.androiddevfaq.App
import com.example.androiddevfaq.R
import com.example.androiddevfaq.base.BaseFragment
import com.example.androiddevfaq.base.observe
import com.example.androiddevfaq.databinding.FragmentAnswerBinding
import com.example.androiddevfaq.ui.answer.interactor.AnswerInteractor
import com.example.androiddevfaq.ui.answer.viewmodel.AnswerFactory
import com.example.androiddevfaq.ui.answer.viewmodel.AnswerViewModel
import io.realm.Realm

class AnswerFragment(
    layoutID: Int = R.layout.fragment_answer
) : BaseFragment<FragmentAnswerBinding>(layoutID, FragmentAnswerBinding::inflate) {

    private lateinit var answerViewModel: AnswerViewModel

    private val stateObserver = Observer<AnswerViewModel.ViewState> {
        binding.apply {
            mainProgressBar.isVisible = it.progressBarVisibility
            titleQuestionTextView.text = it.questionText
            answer.text = it.answerText
            titleQuestionTextView.isVisible = it.questionVisibility
            answer.isVisible = it.answerVisibility
            errorTextView.text = it.errorText ?: requireContext().resources.getString(R.string.answer_error)
            errorTextView.isVisible = it.errorTextViewVisibility
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val questionID = arguments?.getInt(TAG_FOR_QUESTION_ID, 0) ?: 0

        val isFromDatabase = arguments?.getBoolean(TAG_FOR_FROM_DATABASE, false) ?: false

        val realm = Realm.getDefaultInstance()
        val interactor = AnswerInteractor(App.getApi(), realm)
        val factory = AnswerFactory(isFromDatabase, questionID, interactor)
        answerViewModel = ViewModelProviders.of(this, factory)
            .get(AnswerViewModel::class.java)
        observe(answerViewModel.stateLiveData, stateObserver)
        answerViewModel.onActivityCreated(savedInstanceState == null)

        binding.addFavouritesButton.setOnClickListener {
            answerViewModel.onAddQuestionButtonClicked()
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