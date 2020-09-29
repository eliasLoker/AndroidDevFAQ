package com.example.androiddevfaq.ui.addquestion.interactor

import com.example.androiddevfaq.api.Api
import com.example.androiddevfaq.model.ResponseSrc
import com.example.androiddevfaq.utils.ResultWrapper
import com.example.androiddevfaq.utils.mapper.AdapterMapper
import com.example.androiddevfaq.utils.mapper.ResponseDstMapper
import com.example.androiddevfaq.utils.mapper.ResponseDstMapper.Companion.toAddQuestionDst

class AddQuestionInteractor(
    private var api: Api
) {

    suspend fun addQuestion(categoryID: Int, question: String, answer: String) : ResultWrapper<ResponseDstMapper.AddQuestionDst>{
        return try {
            ResultWrapper.Success(api.addQuestion(categoryID, question, answer).toAddQuestionDst())
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }
}