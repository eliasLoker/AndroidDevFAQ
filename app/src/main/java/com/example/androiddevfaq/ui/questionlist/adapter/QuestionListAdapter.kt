package com.example.androiddevfaq.ui.questionlist.adapter

import android.util.Log
import android.view.ViewGroup
import com.example.androiddevfaq.databinding.ItemQuestionListFirstBinding
import com.example.androiddevfaq.databinding.ItemQuestionListSecondBinding
import com.example.androiddevfaq.base.BaseAdapter
import com.example.androiddevfaq.utils.mapper.AdapterMapper

class QuestionListAdapter(
    private val questionListListener: QuestionListListener
) : BaseAdapter<QuestionListHolders.BaseQuestionListHolder>() {

    private var questionList = listOf<AdapterMapper.QuestionItemRecycler>()

    fun setData(newList: List<AdapterMapper.QuestionItemRecycler>) {
        questionList = newList
        Log.d("ActionDebug", "RV: $newList")
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
        holder.bind(questionList[position].name, questionList[position].date, questionList[position].rating)
//        holder.bind2(questionList[position].name, questionList[position].rating)
        holder.itemView.setOnClickListener {
            questionListListener.onClick(position)
        }
    }

    override fun getItemCount() = questionList.size

    override fun getItemViewType(position: Int) = questionList[position].recyclerType
}