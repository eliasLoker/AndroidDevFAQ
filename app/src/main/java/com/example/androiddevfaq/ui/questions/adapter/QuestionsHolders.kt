package com.example.androiddevfaq.ui.questions.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.androiddevfaq.databinding.ItemQuestionListFirstBinding
import com.example.androiddevfaq.databinding.ItemQuestionListSecondBinding

class QuestionsHolders {

    abstract class BaseQuestionListHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        abstract val questionTextView: TextView

        abstract val ratingTextView: TextView

        abstract val dateTextView: TextView

//        abstract fun bind2(questionText: String, rating: Int)

        fun bind(questionText: String, date: String, rating: String) {
            questionTextView.text = questionText
            ratingTextView.text = rating
            dateTextView.text = date
        }
    }

    class FirstTypeItem(binding: ItemQuestionListFirstBinding) : BaseQuestionListHolder(binding) {
        override val questionTextView = binding.itemQuestion

        override val ratingTextView = binding.itemRating

        override val dateTextView = binding.dateTextView

        //        override fun bind(questionText: String) {
//            questionTextView.text = questionText
//        }
//        override fun bind2(questionText: String, rating: Int) {
//            questionTextView.text = questionText
//            ratingTextView.text = "$rating"
//        }
    }

    class SecondTypeItem(binding: ItemQuestionListSecondBinding) : BaseQuestionListHolder(binding) {
        override val questionTextView = binding.itemQuestion

        override val ratingTextView = binding.itemRating

        override val dateTextView = binding.dateTextView

//        override fun bind(questionText: String) {
//            questionTextView.text = questionText
//        }

//        override fun bind2(questionText: String, rating: Int) {
//            questionTextView.text = questionText
//            ratingTextView.text = "$rating"
//        }
    }

}