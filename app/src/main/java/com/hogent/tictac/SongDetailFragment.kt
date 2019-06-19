package com.hogent.tictac

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hogent.tictac.view.SongViewModel
import com.hogent.tictac.view.ChordAdapter
import kotlinx.android.synthetic.main.fragment_song_chords.*
import kotlinx.android.synthetic.main.fragment_song_chords.chord_list
import kotlinx.android.synthetic.main.fragment_song_detail.*
import java.lang.IllegalStateException
import android.media.MediaPlayer.OnCompletionListener


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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chord_list.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = ChordAdapter(
                    songViewModel.songSelected.value!!.chords.map { c -> c.name },
                    null
            )
        }

        if (songViewModel.songSelected.value != null)
            play_button.setOnClickListener {
                for (chord in songViewModel.songSelected.value!!.chords) {
                    val chordId = resources.getIdentifier(chord.name.toLowerCase(), "raw", activity!!.packageName)
                    if (chordId != 0) {
                        if(mediaPlayer.isPlaying)
                            mediaPlayer.stop()
                        mediaPlayer = MediaPlayer.create(activity, chordId)
                        mediaPlayer.start()
                        mediaPlayer.setOnCompletionListener { mp -> mp.release() }
                    }
                    Thread.sleep(1000)
                }
            }
    }
}
