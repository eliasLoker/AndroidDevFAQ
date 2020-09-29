package com.example.androiddevfaq.ui.questionlist.event

import com.example.androiddevfaq.utils.mapper.AdapterMapper

class SetQuestionListAdapterEvent(
    val questionList: List<AdapterMapper.QuestionItemRecycler>
)