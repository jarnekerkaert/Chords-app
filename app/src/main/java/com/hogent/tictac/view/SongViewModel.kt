package com.hogent.tictac.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hogent.tictac.common.Model
import com.hogent.tictac.persistence.SongRepository
import javax.inject.Inject

class SongViewModel : ViewModel() {
    private var songs: MutableList<Model.Song> = ArrayList()
    var songCreating = MutableLiveData<Model.Song>()

    @Inject
    lateinit var songRepository: SongRepository

    init {
        App.com
    }

    fun allSongs(): List<Model.Song> {
        return songs
    }

    fun saveSong() {
        songs.add(songCreating.value!!)
    }
}