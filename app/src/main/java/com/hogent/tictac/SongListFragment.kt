package com.hogent.tictac

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_song_list.*

class SongListFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_song_list, container, false)

        songViewModel = activity?.run {
            ViewModelProviders.of(this).get(SongViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        navController = this.findNavController()

        val actionBar: ActionBar? = (activity as MainActivity).supportActionBar
        actionBar?.title = "Song list"
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setDisplayShowHomeEnabled(false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        song_list.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = SongAdapter(songViewModel.allSongs())
        }

        song_list_create.setOnClickListener {
            navController.navigate(R.id.action_songListFragment_to_createSongFragment)
        }
    }
}
