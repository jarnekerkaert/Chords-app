package com.hogent.tictac.view

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.hogent.tictac.MainActivity
import com.hogent.tictac.R
import com.hogent.tictac.persistence.Model
import com.hogent.tictac.viewmodel.ChordAdapter
import com.hogent.tictac.viewmodel.SongViewModel
import com.hogent.tictac.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.dialog_song_create.view.*
import kotlinx.android.synthetic.main.fragment_song_chords.*
import java.util.*

/**
 * Fragment for creating a song
 *
 * Uses two recycler views:
 * - chords to choose from
 * - chosen chords
 *
 * @property userViewModel viewModel for getting user data
 * @property songViewModel viewModel for setting created song data
 * @property navController navigation controller for navigating to other fragments
 * @property mediaPlayer for playing chords when clicked
 */
class SongChordsFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var userViewModel: UserViewModel
    private lateinit var navController: NavController

    /**
     * Initializes viewmodels, mediaplayer and navigation controller. Also adjusts actionbar accordingly
     */
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

    /**
     * Initialization of the recycler views and floating action buttons.
     *
     * Creates a default song with the default key of C
     * When the adapters are attached to the recycler views, the onclicklisteners are also initialized.
     *
     * When a chord in all_chords_list is clicked, the chord is added to the selected song in songViewModel,
     * The chords in the adapter are reloaded, the chord is played and finally a toast message showing the clicked chord
     * is displayed.
     *
     * When a chord in chords_list is clicked, only the chord is played.
     *
     * When the key selection button is clicked, a custom number picker is created with all major and minor chords. When
     * the save button is clicked, the key in the selected song in songViewModel is set to the selected chord, and the
     * chords in all_chords_list are reloaded.
     *
     * When the save song button is clicked, a form dialog is shown where the user is asked to fill in a title. When saved,
     * the title is set, the song is saved to the database and the navigation controller tries to navigate to the home screen.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songViewModel.songSelected.value = Model.Song(
                Model.Note.C,
                "",
                userViewModel.currentUser.value?.name ?: "unknown",
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
            val snapHelper: SnapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)
            adapter = ChordAdapter(
                    viewLifecycleOwner, songViewModel, false,
                    object : ChordAdapter.OnChordClickListener {
                        override fun onChordClick(item: String) {
                            playChord(item)
                        }
                    }
            )
        }

        song_choose_key.setOnClickListener {
            val keyPicker = NumberPicker(activity)
            keyPicker.displayedValues = Model.Note.values().map { n -> n.name }.toTypedArray()
            keyPicker.minValue = 0
            keyPicker.maxValue = Model.Note.values().size - 1
            keyPicker.value = songViewModel.songSelected.value?.key?.ordinal ?: 0
            AlertDialog.Builder(activity as MainActivity)
                    .setTitle("Pick a key")
                    .setView(keyPicker)
                    .setPositiveButton(
                            "Save"
                    ) { _, _ ->
                        songViewModel.songSelected.value?.key = Model.Note.values()[keyPicker.value]
                        (all_chords_list.adapter as ChordAdapter).reloadData()
                    }.show()
        }

        song_chords_save.setOnClickListener {
            val saveView = LayoutInflater.from(activity).inflate(R.layout.dialog_song_create, null)
            AlertDialog.Builder(activity as MainActivity)
                    .setTitle("Give it a name")
                    .setView(saveView)
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

    /**
     * Plays the given chord using mediaPlayer
     *
     * Looks up the chord file and try to play it. If the mediaPlayer is currently playing a chord then cancel it.
     *
     * @param item the given chord
     */
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