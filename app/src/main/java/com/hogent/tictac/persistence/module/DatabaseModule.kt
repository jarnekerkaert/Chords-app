package com.hogent.tictac.persistence.module

import android.app.Application
import android.content.Context
import com.hogent.tictac.persistence.SongDao
import com.hogent.tictac.persistence.SongRepository
import com.hogent.tictac.persistence.database.SongDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(private val application: Application) {

    @Provides
    @Singleton
    internal fun provideSongRepository(userDao: SongDao): SongRepository {
        return SongRepository(userDao)
    }

    @Provides
    @Singleton
    internal fun provideSongDao(songDatabase: SongDatabase): SongDao {
        return songDatabase.songDao()
    }

    @Provides
    @Singleton
    internal fun provideSongDatabase(context: Context): SongDatabase {
        return SongDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return application
    }

}