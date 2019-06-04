package com.hogent.tictac

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_create_song.*
import kotlinx.android.synthetic.main.fragment_song_list.app_navigation


class CreateSongFragment : Fragment() {

    private lateinit var songService: SongService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_song, container, false)

        songService = (activity as MainActivity).songService

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(
            context!!, R.layout.dropdown_menu_popup_item,
            Note.values()
        )

        val editTextFilledExposedDropdown = song_key
        editTextFilledExposedDropdown.setAdapter(adapter)

        app_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.song_create -> {
                    songService.createSong(Model.Song(song_key.text.toString(), song_chord.text.toString()))

                    (activity as MainActivity).navigateSongList()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false

            }
        }
    }
}
