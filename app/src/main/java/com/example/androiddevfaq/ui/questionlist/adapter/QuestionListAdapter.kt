package com.example.androiddevfaq.ui.questionlist.adapter

import android.view.ViewGroup
import com.example.androiddevfaq.databinding.ItemCategorySecondTypeBinding
import com.example.androiddevfaq.databinding.ItemQuestionListFirstBinding
import com.example.androiddevfaq.databinding.ItemQuestionListSecondBinding
import com.example.androiddevfaq.ui.base.BaseAdapter
import com.example.androiddevfaq.utils.mapper.AdapterMapper
import com.example.androiddevfaq.utils.mapper.ResponseDstMapper

class QuestionListAdapter(
    private val questionListListener: QuestionListListener
) : BaseAdapter<QuestionListHolders.BaseQuestionListHolder>() {

    private var questionList = listOf<AdapterMapper.QuestionItemRecycler>()

    fun setData(newList: List<AdapterMapper.QuestionItemRecycler>) {
        questionList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionListHolders.BaseQuestionListHolder {
        return when(viewType) {
            0 -> QuestionListHolders.FirstTypeItem(parent.inflateBinding(ItemQuestionListFirstBinding::inflate))
            else -> QuestionListHolders.SecondTypeItem(parent.inflateBinding(ItemQuestionListSecondBinding::inflate))
        }
    }

    override fun onBindViewHolder(
        holder: QuestionListHolders.BaseQuestionListHolder,
        position: Int
    ) {
        holder.bind(questionList[position].name, questionList[position].rating)
        holder.itemView.setOnClickListener {
            questionListListener.onClick(position)
        }
    }

    override fun getItemCount() = questionList.size

    override fun getItemViewType(position: Int) = questionList[position].recyclerType
}