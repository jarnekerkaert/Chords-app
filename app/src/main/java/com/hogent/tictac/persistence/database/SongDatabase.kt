package com.hogent.tictac.persistence.database

import android.content.Context
import androidx.room.*
import com.hogent.tictac.common.Model
import com.hogent.tictac.persistence.Converters
import com.hogent.tictac.persistence.SongDao

@Database(entities = [Model.Song::class], version = 1)
@TypeConverters(Converters::class)
abstract class SongDatabase: RoomDatabase() {

    abstract fun songDao(): SongDao

    companion object {
        @Volatile
        private var INSTANCE: SongDatabase? = null

        fun getDatabase(context: Context): SongDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabase::class.java,
                    "Song_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}