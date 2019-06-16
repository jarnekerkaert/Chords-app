package com.hogent.tictac.view

import androidx.lifecycle.MutableLiveData
import com.hogent.tictac.common.Model
import com.hogent.tictac.persistence.InjectedViewModel
import com.hogent.tictac.persistence.SongRepository
import javax.inject.Inject

class SongViewModel : InjectedViewModel() {
    var songCreating = MutableLiveData<Model.Song>()

    @Inject
    lateinit var songRepository: SongRepository

    fun allSongs(): List<Model.Song> {
        return songRepository.songs.value ?: arrayListOf()
    }

    fun saveSong() {
        songRepository.insert(songCreating.value!!)
    }
}