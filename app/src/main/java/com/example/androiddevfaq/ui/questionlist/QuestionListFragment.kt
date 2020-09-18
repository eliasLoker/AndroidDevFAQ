package com.example.androiddevfaq.ui.questionlist

import android.os.Bundle
import com.example.androiddevfaq.R
import com.example.androiddevfaq.databinding.FragmentQuestionListBinding
import com.example.androiddevfaq.ui.BaseFragment

class QuestionListFragment(
    layoutID: Int = R.layout.fragment_question_list,

    ) : BaseFragment<FragmentQuestionListBinding>(layoutID, FragmentQuestionListBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }


    companion object {
        @JvmStatic
        fun getBundle(param1: String, param2: String) = Bundle().apply {
//            putString(ARG_PARAM1, param1)
//            putString(ARG_PARAM2, param2)
        }

    }
}