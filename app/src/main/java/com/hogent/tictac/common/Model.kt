package com.hogent.tictac.common

import androidx.room.Entity
import androidx.room.PrimaryKey

object Model {

    @Entity
    data class Song(
        var key: Note,
        var title: String,
        var chords: ArrayList<Note> = arrayListOf(),
        @PrimaryKey(autoGenerate = true) val id: Int
    )
}