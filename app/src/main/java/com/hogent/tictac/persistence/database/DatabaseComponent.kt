package com.hogent.tictac.persistence.database

import com.hogent.tictac.view.SongViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
interface DatabaseComponent {
    fun inject(app: App)
    fun inject(userViewModel: SongViewModel)
}