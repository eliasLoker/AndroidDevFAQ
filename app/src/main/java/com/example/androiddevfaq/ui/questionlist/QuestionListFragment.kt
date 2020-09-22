package com.example.androiddevfaq.ui.questionlist

import android.os.Bundle
import com.example.androiddevfaq.R
import com.example.androiddevfaq.databinding.FragmentQuestionListBinding
import com.example.androiddevfaq.ui.base.BaseFragment

class QuestionListFragment(
    layoutID: Int = R.layout.fragment_question_list
) : BaseFragment<FragmentQuestionListBinding>(layoutID, FragmentQuestionListBinding::inflate) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val arg = arguments?.getInt(TAG_FOR_CATEGORY_ID) ?: 0
        binding.title.text = "CAT ID: $arg"
    }

    companion object {

        private const val TAG_FOR_CATEGORY_ID = "TAG_FOR_CATEGORY_ID"

        @JvmStatic
        fun getBundle(categoryID: Int) = Bundle().apply {
            putInt(TAG_FOR_CATEGORY_ID, categoryID)
        }
    }
}