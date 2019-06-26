package com.hogent.tictac.view

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
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
import kotlinx.android.synthetic.main.fragment_song_chords.*


class SongChordsFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var navController: NavController
    private lateinit var mediaPlayer: MediaPlayer

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

        chord_list.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = ChordAdapter(
                viewLifecycleOwner, songViewModel, true,
                object : ChordAdapter.OnChordClickListener {
                override fun onChordClick(item: String) {
                    songViewModel.songSelected.value?.chords?.add(Model.Note.valueOf(item))

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

                    Toast.makeText(activity, "Added $item to song", Toast.LENGTH_SHORT).show()
                }
            })
        }

        song_chords_save.setOnClickListener {
            songViewModel.saveSong(userViewModel.databaseUser.value!!.id)
            navController.navigate(R.id.action_songChordsFragment_to_songDetailFragment)
        }
    }
}