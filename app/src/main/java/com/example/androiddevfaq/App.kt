package com.example.androiddevfaq

import android.app.Application
import com.example.androiddevfaq.api.Api
import com.example.androiddevfaq.api.FakeApiImpl

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        private var instance: App? = null

        private var api: Api? = null

        @JvmStatic
        fun getInstance() : App {
            return instance as App
        }

        @JvmStatic
        fun getApi() = when(api) {
                null -> {
                    api = FakeApiImpl.ApiBuilder()
                        .setDelay(1000)
                        .build()
                    api!!
                }
                else -> api!!
            }

    }
}