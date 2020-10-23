package com.example.androiddevfaq.ui.addquestion.interactor

import com.example.androiddevfaq.api.Api
import com.example.androiddevfaq.model.ResponseSrc
import com.example.androiddevfaq.utils.ResultWrapper

class AddQuestionInteractor(
    private var api: Api
) {

    suspend fun addQuestion(categoryID: Int, question: String, answer: String) : ResultWrapper<ResponseSrc.AddQuestionSrc>{
        return try {
            ResultWrapper.Success(api.addQuestion(categoryID, question, answer))
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }
}