package com.example.androiddevfaq.ui.category.interactor

import com.example.androiddevfaq.api.Api
import com.example.androiddevfaq.utils.mapper.ResponseDstMapper
import com.example.androiddevfaq.utils.mapper.ResponseDstMapper.Companion.toCategoryResponseDst
import com.example.androiddevfaq.utils.ResultWrapper
import java.lang.Exception

class CategoryInteractor(
    private val api: Api
) {

    suspend fun getCategories(): ResultWrapper<ResponseDstMapper.CategoryDst> {
        return try {
            ResultWrapper.Success(api.getCategories().toCategoryResponseDst())
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }

}