package com.example.androiddevfaq.ui.question

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.androiddevfaq.App
import com.example.androiddevfaq.R
import com.example.androiddevfaq.databinding.FragmentQuestionBinding
import com.example.androiddevfaq.base.BaseFragment
import com.example.androiddevfaq.ui.question.interactor.QuestionInteractor
import com.example.androiddevfaq.ui.question.viewmodel.QuestionFactory
import com.example.androiddevfaq.ui.question.viewmodel.QuestionViewModel
import com.example.androiddevfaq.ui.question.viewmodel.QuestionViewModelImpl

class QuestionFragment(
    layoutID: Int = R.layout.fragment_question
) : BaseFragment<FragmentQuestionBinding>(layoutID, FragmentQuestionBinding::inflate) {

    private lateinit var questionViewModel: QuestionViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val questionID = arguments?.getInt(TAG_FOR_QUESTION_ID, 0) ?: 0
        val interactor = QuestionInteractor(App.getApi())
        val factory = QuestionFactory(questionID, interactor)
        questionViewModel = ViewModelProviders.of(this, factory).get(QuestionViewModelImpl::class.java)
        questionViewModel.onActivityCreated()
        initObservers()
    }

    private fun initObservers() {
        questionViewModel.title.observe(viewLifecycleOwner, {
            binding.titleQuestionTextView.text = it
        })

        questionViewModel.answer.observe(viewLifecycleOwner, {
            binding.answer.text = it
        })
    }

    companion object {

        private const val TAG_FOR_QUESTION_ID = "TAG_FOR_QUESTION_ID"

        @JvmStatic
        fun getBundle(questionID: Int) = Bundle().apply {
            putInt(TAG_FOR_QUESTION_ID, questionID)
        }
    }
}