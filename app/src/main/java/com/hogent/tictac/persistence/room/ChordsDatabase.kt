package com.hogent.tictac.persistence.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hogent.tictac.persistence.Converters
import com.hogent.tictac.persistence.Model

@Database(entities = [Model.Song::class, Model.User::class], version = 5)
@TypeConverters(Converters::class)
abstract class ChordsDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: ChordsDatabase? = null

        fun getDatabase(context: Context): ChordsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ChordsDatabase::class.java,
                        "Song_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}