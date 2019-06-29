package com.hogent.tictac.view

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hogent.tictac.MainActivity
import com.hogent.tictac.R
import com.hogent.tictac.persistence.Model
import com.hogent.tictac.viewmodel.ChordAdapter
import com.hogent.tictac.viewmodel.SongViewModel
import kotlinx.android.synthetic.main.fragment_song_chords.chord_list
import kotlinx.android.synthetic.main.fragment_song_detail.*


class SongDetailFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var navController: NavController
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_song_detail, container, false)

        songViewModel = activity?.run {
            ViewModelProviders.of(this).get(SongViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        mediaPlayer = MediaPlayer()
        navController = this.findNavController()

        songViewModel.songSelected.observe(this, Observer<Model.Song?> {
            (activity as MainActivity).supportActionBar?.title = it?.title
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chord_list.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter =
                    ChordAdapter(viewLifecycleOwner, songViewModel, false, object : ChordAdapter.OnChordClickListener {
                        override fun onChordClick(item: String) {

                        }
                    })
        }

        play_button.setOnClickListener {
            if (songViewModel.songSelected.value != null) {
                for (chord in songViewModel.songSelected.value!!.chords) {
                    val chordId = resources.getIdentifier(chord.name.toLowerCase(), "raw", activity!!.packageName)
                    if (chordId != 0) {
                        mediaPlayer = MediaPlayer.create(activity, chordId)
                        if (mediaPlayer.isPlaying) {
                            mediaPlayer.reset()
                        }
                        mediaPlayer.setOnPreparedListener { mp ->
                            mp.start()
                            Thread.sleep(1000)
                        }
                        mediaPlayer.setOnCompletionListener { mp -> mp.reset() }
                    }
                }
                mediaPlayer.release()
            }
        }
    }
}