package com.hogent.tictac.persistence.room

import android.content.Context
import androidx.room.*
import com.hogent.tictac.persistence.Model
import com.hogent.tictac.persistence.Converters

@Database(entities = [Model.Song::class, Model.User::class], version = 3)
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