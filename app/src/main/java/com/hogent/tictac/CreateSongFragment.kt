package com.hogent.tictac

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.hogent.tictac.common.Model
import com.hogent.tictac.common.Note
import com.hogent.tictac.view.SongViewModel
import kotlinx.android.synthetic.main.fragment_create_song.*
import java.util.*


class CreateSongFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_song, container, false)

        songViewModel = activity?.run {
            ViewModelProviders.of(this).get(SongViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        navController = this.findNavController()

        val actionBar: ActionBar? = (activity as MainActivity).supportActionBar
        actionBar?.title = "Create song"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(
            context!!, R.layout.dropdown_menu_popup_item,
            Note.values()
        )

        song_key.setAdapter(adapter)

        song_create.setOnClickListener {
            if (song_key.text.isEmpty())
                Toast.makeText(context!!, "Select a key", Toast.LENGTH_SHORT).show()
            else {
                songViewModel.songCreating.value = Model.Song(
                    Note.valueOf(song_key.text.toString().toUpperCase()),
                    song_chord.text.toString(),
                    arrayListOf(),
                    UUID.randomUUID().variant()
                )

                navController.navigate(R.id.action_createSongFragment_to_songChordsFragment)
            }
        }
    }
}