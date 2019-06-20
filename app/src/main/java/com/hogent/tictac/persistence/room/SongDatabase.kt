package com.hogent.tictac.persistence.room

import android.content.Context
import androidx.room.*
import com.hogent.tictac.persistence.Model
import com.hogent.tictac.persistence.Converters

@Database(entities = [Model.Song::class], version = 2)
@TypeConverters(Converters::class)
abstract class SongDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

    companion object {
        @Volatile
        private var INSTANCE: SongDatabase? = null

        fun getDatabase(context: Context): SongDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "Song_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}