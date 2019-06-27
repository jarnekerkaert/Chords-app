package com.hogent.tictac.persistence.room

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(private val application: Application) {

    @Provides
    @Singleton
    internal fun provideSongRepository(songDao: SongDao): SongRepository {
        return SongRepository(songDao)
    }

    @Provides
    @Singleton
    internal fun provideSongDao(chordsDatabase: ChordsDatabase): SongDao {
        return chordsDatabase.songDao()
    }

    @Provides
    @Singleton
    internal fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }

    @Provides
    @Singleton
    internal fun provideUserDao(chordsDatabase: ChordsDatabase): UserDao {
        return chordsDatabase.userDao()
    }

    @Provides
    @Singleton
    internal fun provideSongDatabase(context: Context): ChordsDatabase {
        return ChordsDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return application
    }

}