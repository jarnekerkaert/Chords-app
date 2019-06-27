package com.hogent.tictac.persistence

import com.hogent.tictac.persistence.network.NetworkModule
import com.hogent.tictac.persistence.room.DatabaseModule
import com.hogent.tictac.viewmodel.SongViewModel
import com.hogent.tictac.viewmodel.UserViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, NetworkModule::class])
interface DatabaseComponent {
    fun inject(app: App)
    fun inject(songViewModel: SongViewModel)
    fun inject(userViewModel: UserViewModel)
}