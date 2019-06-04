package com.hogent.tictac

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_song_list.*

class SongListFragment : Fragment() {

    private lateinit var songService: SongService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_song_list, container, false)

        songService = (activity as MainActivity).songService

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        song_list.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = SongAdapter(songService.allSongs())
        }

        app_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.song_new -> {
                    (activity as MainActivity).navigateCreateSong()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }
}
