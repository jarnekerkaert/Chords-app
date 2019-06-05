package com.hogent.tictac

import android.view.View
import androidx.lifecycle.ViewModel
import com.hogent.tictac.common.Model
import com.hogent.tictac.common.SingleLiveEvent

class SongViewModel : ViewModel() {
    private var songs: MutableList<Model.Song> = ArrayList()
    val createSongEvent = SingleLiveEvent<Unit>()

    init {
        songs.add(Model.Song("C","Lays makes milk now"))
        songs.add(Model.Song("C","Chocolate is the new steak"))
    }

    fun allSongs(): List<Model.Song> {
        return songs
    }

    fun createSong(song: Model.Song) {
        songs.add(song)
    }

    fun onCreateSong(button: View) {
        createSongEvent.call()
    }
}