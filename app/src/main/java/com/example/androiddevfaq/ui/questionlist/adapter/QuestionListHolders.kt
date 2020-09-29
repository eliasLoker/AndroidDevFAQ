package com.example.androiddevfaq.ui.questionlist.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.androiddevfaq.databinding.ItemQuestionListFirstBinding
import com.example.androiddevfaq.databinding.ItemQuestionListSecondBinding

class QuestionListHolders {

    abstract class BaseQuestionListHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        abstract val questionTextView: TextView

        abstract val ratingTextView: TextView

        abstract fun bind(questionText: String, rating: Int)
    }

    class FirstTypeItem(binding: ItemQuestionListFirstBinding) : BaseQuestionListHolder(binding) {
        override val questionTextView = binding.itemQuestion

        override val ratingTextView = binding.itemRating

        //        override fun bind(questionText: String) {
//            questionTextView.text = questionText
//        }
        override fun bind(questionText: String, rating: Int) {
            questionTextView.text = questionText
            ratingTextView.text = "$rating"
        }
    }

    class SecondTypeItem(binding: ItemQuestionListSecondBinding) : BaseQuestionListHolder(binding) {
        override val questionTextView = binding.itemQuestion

        override val ratingTextView = binding.itemRating

//        override fun bind(questionText: String) {
//            questionTextView.text = questionText
//        }

        override fun bind(questionText: String, rating: Int) {
            questionTextView.text = questionText
            ratingTextView.text = "$rating"
        }
    }

}