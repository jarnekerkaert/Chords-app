package com.hogent.tictac.common

object Model {
    data class Song (
        var key: Note,
        var title: String,
        var chords: ArrayList<Note>?
    )
}