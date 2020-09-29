package com.example.androiddevfaq.ui.question.interactor

import com.example.androiddevfaq.api.Api
import com.example.androiddevfaq.utils.ResultWrapper
import com.example.androiddevfaq.utils.mapper.ResponseDstMapper
import com.example.androiddevfaq.utils.mapper.ResponseDstMapper.Companion.toQuestionDst
import java.lang.Exception

class QuestionInteractor(
    private val api: Api
) {

    suspend fun getQuestion(questionID: Int) : ResultWrapper<ResponseDstMapper.QuestionDst> {
        return try {
            ResultWrapper.Success(api.getQuestion(questionID).toQuestionDst())
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }
}