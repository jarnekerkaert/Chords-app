package com.hogent.tictac

import com.hogent.tictac.common.Model

class SongService {

    private var songs: MutableList<Model.Song> = ArrayList()

    init {
        songs.add(Model.Song("C","hey hye"))
        songs.add(Model.Song("C","sdljfm"))
    }

    fun allSongs(): List<Model.Song> {
        return songs
    }

    fun createSong(song: Model.Song) {
        songs.add(song)
    }
}