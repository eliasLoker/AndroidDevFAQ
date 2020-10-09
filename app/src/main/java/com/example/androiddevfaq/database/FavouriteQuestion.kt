package com.example.androiddevfaq.database

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class FavouriteQuestion(
    @PrimaryKey var id: Int = 0,
    var question: String = "",
    var answer: String = "",
    var addDateTimestamp: Long = 0L
) : RealmObject()