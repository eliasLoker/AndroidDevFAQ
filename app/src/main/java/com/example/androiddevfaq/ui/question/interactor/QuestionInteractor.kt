package com.example.androiddevfaq.ui.question.interactor

import android.util.Log
import com.example.androiddevfaq.api.Api
import com.example.androiddevfaq.database.FavouriteQuestion
import com.example.androiddevfaq.utils.ResultWrapper
import com.example.androiddevfaq.utils.mapper.ResponseDstMapper
import com.example.androiddevfaq.utils.mapper.ResponseDstMapper.Companion.toQuestionDst
import io.realm.Realm
import java.lang.Exception

class QuestionInteractor(
    private val api: Api,
    private val realm: Realm
) {

    suspend fun getQuestion(questionID: Int) : ResultWrapper<ResponseDstMapper.QuestionDst> {
        return try {
            ResultWrapper.Success(api.getQuestion(questionID).toQuestionDst())
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }

    fun addToFavouritesQuestion(questionID: Int, question: String, answer: String) {
        realm.use {
            it.executeTransaction {
                val timestamp = System.currentTimeMillis()
                val favouriteQuestion = FavouriteQuestion(questionID, question, answer, timestamp)
                realm.copyToRealm(favouriteQuestion)
            }
        }
    }

    fun getPairQuestion(questionID: Int) : Pair<String, String> {
        val favouriteQuestion = realm
            .where(FavouriteQuestion::class.java)
            .equalTo("id", questionID)
            .findFirst()
        favouriteQuestion?.let {
            return Pair(it.question, it.answer)
        }
        return Pair("", "")
    }
}