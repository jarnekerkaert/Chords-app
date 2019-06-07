package com.hogent.tictac.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hogent.tictac.common.Model
import com.hogent.tictac.common.Note

class SongViewModel : ViewModel() {
    private var songs: MutableList<Model.Song> = ArrayList()
    var songCreating = MutableLiveData<Model.Song>()

    init {
        songs.add(Model.Song(Note.C, "Lays makes milk now", null))
        songs.add(Model.Song(Note.C, "Chocolate is the new steak", null))
    }

    fun allSongs(): List<Model.Song> {
        return songs
    }

    fun saveSong() {
        songs.add(songCreating.value!!)
    }
}