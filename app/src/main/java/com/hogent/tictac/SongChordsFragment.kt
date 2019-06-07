package com.hogent.tictac

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.hogent.tictac.common.Note
import kotlinx.android.synthetic.main.fragment_song_chords.*

class SongChordsFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var navController: NavController
    private var listener: OnSongChordsFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_song_chords, container, false)

        songViewModel = activity?.run {
            ViewModelProviders.of(this).get(SongViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        navController = this.findNavController()

        val actionBar: ActionBar? = (activity as MainActivity).supportActionBar
        actionBar?.title = "Song detail"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chord_list.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = ChordAdapter(Note.values().map { n -> n.name }, listener)
        }

        song_chords_save.setOnClickListener {
            songViewModel.saveSong()

            navController.navigate(R.id.action_songChordsFragment_to_songDetailFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSongChordsFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnSongChordsFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnSongChordsFragmentInteractionListener {
        fun onSongChordsFragmentInteraction(chord: String)
    }
}