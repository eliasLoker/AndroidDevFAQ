package com.example.androiddevfaq.ui.questions.adapter

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.androiddevfaq.R
import com.example.androiddevfaq.databinding.ItemQuestionListFirstBinding
import com.example.androiddevfaq.databinding.ItemQuestionListSecondBinding
import com.example.androiddevfaq.base.BaseAdapter
import com.example.androiddevfaq.ui.questions.model.QuestionsItem

class QuestionsAdapter(
    private val questionsListener: QuestionsListener
) : BaseAdapter<QuestionsHolders.BaseQuestionListHolder>() {

    private var questionList = listOf<QuestionsItem.QuestionsItemDst>()

    fun setData(newList: List<QuestionsItem.QuestionsItemDst>) {
        questionList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionsHolders.BaseQuestionListHolder {
        return when(viewType) {
            0 -> QuestionsHolders.FirstTypeItem(parent.inflateBinding(ItemQuestionListFirstBinding::inflate))
            else -> QuestionsHolders.SecondTypeItem(parent.inflateBinding(ItemQuestionListSecondBinding::inflate))
        }
    }

    override fun onBindViewHolder(
        holder: QuestionsHolders.BaseQuestionListHolder,
        position: Int
    ) {
        val resources = holder.itemView.resources

        val question = questionList[position].name ?: resources.getString(R.string.question_empty_question)
        val date = questionList[position].date
        val rating = "${questionList[position].rating ?: resources.getString(R.string.question_empty_rating)}"
        holder.bind(question, date, rating)

        holder.itemView.setOnClickListener {
            questionsListener.onClick(position)
        }
        setAnimation(holder.itemView)
    }

    override fun getItemCount() = questionList.size

    override fun getItemViewType(position: Int) = questionList[position].recyclerType

    private fun setAnimation(view: View) {
        val anim = AnimationUtils.loadAnimation(view.context, R.anim.recycler)
        view.startAnimation(anim)
    }
}