package com.example.androiddevfaq.ui.category.interactor

import com.example.androiddevfaq.api.Api
import com.example.androiddevfaq.model.ResponseSrc
import com.example.androiddevfaq.utils.ResultWrapper
import java.text.ParseException

class CategoryInteractor(
    private val api: Api
) {

    suspend fun getCategories(): ResultWrapper<ResponseSrc.CategorySrc> {
        return try {
            ResultWrapper.Success(api.getCategories())
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }

}