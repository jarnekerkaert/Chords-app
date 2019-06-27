package com.hogent.tictac.persistence

import android.app.Application
import com.hogent.tictac.persistence.room.DatabaseModule

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