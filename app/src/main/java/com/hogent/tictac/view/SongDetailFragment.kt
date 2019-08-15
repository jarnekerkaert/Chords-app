package com.hogent.tictac.view

import android.annotation.SuppressLint
import android.content.res.AssetFileDescriptor
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
import java.io.IOException


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

    @SuppressLint("ShowToast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chord_list.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter =
                ChordAdapter(viewLifecycleOwner, songViewModel, false, object : ChordAdapter.OnChordClickListener {
                    override fun onChordClick(item: String) {
                        playChord(item)
                    }
                }, R.color.colorPrimaryDark)
        }

        play_button.setOnClickListener {
            if (songViewModel.songSelected.value != null && songViewModel.songSelected.value!!.chords.size != 0) {
                var count = 0
                val chords = songViewModel.songSelected.value!!.chords
                val chordIds = chords.map { c ->
                    resources.getIdentifier(c.name.toLowerCase(), "raw", activity!!.packageName)
                }
                mediaPlayer = MediaPlayer.create(activity, chordIds[count])
                mediaPlayer.setOnCompletionListener { mp ->
                    run {
                        count++
                        if (count < chordIds.size) {
                            try {
                                mp.reset()
                                if (mp.isPlaying)
                                    mp.stop()
                                val chord: AssetFileDescriptor = resources.openRawResourceFd(chordIds[count])
                                mp.setDataSource(chord.fileDescriptor, chord.startOffset, chord.declaredLength)
                                mp.prepare()
                                mp.start()
                                chord.close()
                            } catch (e: IllegalArgumentException) {
                                songViewModel.songToast.value =
                                    "Unable to play audio queue do to exception: " + e.message
                            } catch (e: IllegalStateException) {
                                songViewModel.songToast.value =
                                    "Unable to play audio queue do to exception: " + e.message
                            } catch (e: IOException) {
                                songViewModel.songToast.value =
                                    "Unable to play audio queue do to exception: " + e.message
                            }
                        } else {
                            mp.stop()
                            mp.release()
                        }
                    }
                }
                mediaPlayer.start()
            }
        }
    }

    private fun playChord(item: String) {
        val chordId = resources.getIdentifier(item.toLowerCase(), "raw", activity!!.packageName)
        if (chordId != 0) {
            mediaPlayer = MediaPlayer.create(activity, chordId)
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.reset()
            }
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mp -> mp.release() }
        }
    }
}