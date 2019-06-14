package com.hogent.tictac.common

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hogent.tictac.persistence.Converters

object Model {

    @Entity
    data class Song(
        @TypeConverters(Converters::class)
        var key: Note,
        var title: String,
        @TypeConverters(Converters::class)
        var chords: ArrayList<Note> = arrayListOf(),
        @PrimaryKey(autoGenerate = true) val id: Int
    )
}