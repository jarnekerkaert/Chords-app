package com.hogent.tictac.persistence

import com.hogent.tictac.persistence.database.NetworkModule
import com.hogent.tictac.persistence.room.DatabaseModule
import com.hogent.tictac.viewmodel.SongViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, NetworkModule::class])
interface DatabaseComponent {
    fun inject(app: App)
    fun inject(userViewModel: SongViewModel)
}