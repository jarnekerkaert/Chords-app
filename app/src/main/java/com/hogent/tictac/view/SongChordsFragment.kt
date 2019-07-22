package com.hogent.tictac.view

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hogent.tictac.MainActivity
import com.hogent.tictac.R
import com.hogent.tictac.persistence.Model
import com.hogent.tictac.viewmodel.ChordAdapter
import com.hogent.tictac.viewmodel.SongViewModel
import com.hogent.tictac.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.dialog_song_create.view.*
import kotlinx.android.synthetic.main.fragment_song_chords.*
import java.util.*


class SongChordsFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var userViewModel: UserViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_song_chords, container, false)

        songViewModel = activity?.run {
            ViewModelProviders.of(this).get(SongViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        userViewModel = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        mediaPlayer = MediaPlayer()
        navController = this.findNavController()

        val actionBar: ActionBar? = (activity as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songViewModel.songSelected.value = Model.Song(
            Model.Note.C,
            "",
            mutableListOf(),
            UUID.randomUUID().toString()
        )

        all_chords_list.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = ChordAdapter(
                viewLifecycleOwner, songViewModel, true,
                object : ChordAdapter.OnChordClickListener {
                    override fun onChordClick(item: String) {
                        songViewModel.songSelected.value?.chords?.add(Model.Note.valueOf(item))

                        (chord_list.adapter as ChordAdapter).reloadData()

                        playChord(item)

                        Toast.makeText(activity, "Added $item to song", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        chord_list.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = ChordAdapter(
                viewLifecycleOwner, songViewModel, false,
                object : ChordAdapter.OnChordClickListener {
                    override fun onChordClick(item: String) {
                        playChord(item)
                    }
                }
            )
        }

        song_chords_save.setOnClickListener {
            val view = LayoutInflater.from(activity).inflate(R.layout.dialog_song_create, null)
            AlertDialog.Builder(activity as MainActivity)
                .setTitle("Give it a name")
                .setView(view)
                .setPositiveButton(
                    "Create"
                ) { _, _ ->
                    songViewModel.songSelected.value?.title = view.song_name.text.toString()
                    songViewModel.saveSong(userViewModel.databaseUser.value!!.id)
                    navController.navigate(R.id.action_songChordsFragment_to_songDetailFragment)
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, _ ->
                    dialog!!.cancel()
                }.show()
        }
    }

    private fun playChord(item: String) {
        val chordId = resources.getIdentifier(item.toLowerCase(), "raw", activity!!.packageName)
        if (chordId != 0) {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.reset()
            }
            mediaPlayer = MediaPlayer.create(activity, chordId)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mp -> mp.reset() }
        }
    }
}