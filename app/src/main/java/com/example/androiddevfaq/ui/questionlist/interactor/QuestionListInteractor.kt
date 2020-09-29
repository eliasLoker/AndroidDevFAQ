package com.example.androiddevfaq.ui.questionlist.interactor

import com.example.androiddevfaq.api.Api
import com.example.androiddevfaq.utils.ResultWrapper
import com.example.androiddevfaq.utils.mapper.ResponseDstMapper
import com.example.androiddevfaq.utils.mapper.ResponseDstMapper.Companion.toQuestionListResponseDst
import java.lang.Exception

class QuestionListInteractor(
    private val api: Api
) {

    suspend fun getQuestionsList(categoryID: Int) : ResultWrapper<ResponseDstMapper.QuestionListDst> {
        return try {
            ResultWrapper.Success(api.getQuestionListByID(categoryID).toQuestionListResponseDst())
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }
}