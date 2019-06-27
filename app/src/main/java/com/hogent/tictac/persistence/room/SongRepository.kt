package com.hogent.tictac.persistence.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.hogent.tictac.persistence.Model

class SongRepository(private val songDao: SongDao) {
    var songs: LiveData<List<Model.Song>> = songDao.getAllSongs()

    @WorkerThread
    fun insert(song: Model.Song) {
        songDao.insert(song)
    }
}