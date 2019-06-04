package com.hogent.tictac

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var songListFragment: SongListFragment
    private lateinit var createSongFragment: CreateSongFragment

    lateinit var songService: SongService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        songService = SongService()

        setContentView(R.layout.activity_main)

        navigateSongList()
    }

    fun createSong(song: Model.Song) {
        songService.createSong(song)
    }

    fun navigateSongList() {
        songListFragment = SongListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.app_main_container, songListFragment)
            .commit()
    }

    fun navigateCreateSong() {
        createSongFragment = CreateSongFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.app_main_container, createSongFragment)
            .commit()
    }
}
