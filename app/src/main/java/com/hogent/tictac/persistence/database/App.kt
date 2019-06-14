package com.hogent.tictac.persistence.database

import android.app.Application
import com.hogent.tictac.persistence.DaggerDatabaseComponent

class App: Application() {

    companion object {
        lateinit var component: DatabaseComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerDatabaseComponent.builder()
            .databaseModule(DatabaseModule(this))
            .build()
    }
}