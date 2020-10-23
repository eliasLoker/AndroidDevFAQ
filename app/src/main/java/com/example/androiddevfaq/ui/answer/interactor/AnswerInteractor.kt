package com.example.androiddevfaq.ui.answer.interactor

import com.example.androiddevfaq.api.Api
import com.example.androiddevfaq.database.FavouriteQuestion
import com.example.androiddevfaq.model.ResponseSrc
import com.example.androiddevfaq.utils.ResultWrapper
import io.realm.Realm

class AnswerInteractor(
    private val api: Api,
    private val realm: Realm
) {

    suspend fun getQuestion(questionID: Int): ResultWrapper<ResponseSrc.QuestionSrc> {
        return try {
            ResultWrapper.Success(api.getQuestion(questionID))
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

    fun getPairQuestion(questionID: Int): Pair<String, String> {
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