package com.hogent.tictac.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hogent.tictac.MainActivity
import com.hogent.tictac.R
import com.hogent.tictac.viewmodel.SongViewModel
import com.hogent.tictac.viewmodel.SongAdapter
import kotlinx.android.synthetic.main.fragment_song_list.*

class SongListFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var navController: NavController
    private lateinit var listener: View.OnClickListener

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_song_list, container, false)

        songViewModel = activity?.run {
            ViewModelProviders.of(this).get(SongViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        songViewModel.retrieveSongs()

        navController = this.findNavController()

        val actionBar: ActionBar? = (activity as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setDisplayShowHomeEnabled(false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener = View.OnClickListener {
            songViewModel.songSelected.value = songViewModel.songs.value?.get(0)
            navController.navigate(R.id.action_songListFragment_to_songDetailFragment)
        }

        song_list.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = SongAdapter(viewLifecycleOwner, songViewModel, listener)
        }

        song_list_create.setOnClickListener {
            navController.navigate(R.id.action_songListFragment_to_createSongFragment)
        }
    }
}
