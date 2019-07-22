package com.hogent.tictac.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

object Model {

    @Entity
    data class Song(
            @TypeConverters(Converters::class)
            var key: Note,
            var title: String,
            var user: String,
            @TypeConverters(Converters::class)
            var chords: MutableList<Note> = arrayListOf(),
            @PrimaryKey var id: String
    )

    @Entity
    data class User(
            @PrimaryKey var id: String,
            var name: String,
            var songs: ArrayList<String> = arrayListOf()
    )

    data class Login(
            var name: String,
            var password: String
    )

    data class Register(
            var name: String,
            var password: String
    )

    enum class NoteMajor {
        C, CS, D, DS, E, F, FS, G, GS, A, AS, B
    }

    enum class Note {
        C, CS, D, DS, E, F, FS, G, GS, A, AS, B, CM, CSM, DM, DSM, EM, FM, FSM, GM, GSM, AM, ASM, BM
    }
}