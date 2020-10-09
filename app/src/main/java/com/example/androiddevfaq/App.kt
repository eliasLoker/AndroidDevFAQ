package com.example.androiddevfaq

import android.app.Application
import com.example.androiddevfaq.api.Api
import com.example.androiddevfaq.api.FakeApiImpl
import com.example.androiddevfaq.database.Migration
import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("myrealm.realm")
            .schemaVersion(Migration.DATABASE_VERSION)
            .migration(Migration())
            .build()
        Realm.setDefaultConfiguration(config)
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
                        .setDelay(0)
                        .build()
                    api!!
                }
                else -> api!!
            }

    }
}