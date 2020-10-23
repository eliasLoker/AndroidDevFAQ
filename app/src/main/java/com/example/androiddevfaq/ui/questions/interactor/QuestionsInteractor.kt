package com.example.androiddevfaq.ui.questions.interactor

import com.example.androiddevfaq.api.Api
import com.example.androiddevfaq.model.ResponseSrc
import com.example.androiddevfaq.utils.ResultWrapper
import java.lang.Exception

class QuestionsInteractor(
    private val api: Api
) {

    suspend fun getQuestionsList(categoryID: Int) : ResultWrapper<ResponseSrc.QuestionListSrc> {
        return try {
            ResultWrapper.Success(api.getQuestionListByID(categoryID))
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }
}