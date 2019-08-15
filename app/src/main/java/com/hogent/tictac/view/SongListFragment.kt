package com.hogent.tictac.view

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
import com.hogent.tictac.MainActivity
import com.hogent.tictac.R
import com.hogent.tictac.viewmodel.SongAdapter
import com.hogent.tictac.viewmodel.SongViewModel
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

        songViewModel.retrieveSongs()
        navController = this.findNavController()

        val actionBar: ActionBar? = (activity as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setDisplayShowHomeEnabled(false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        song_list.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = SongAdapter(viewLifecycleOwner, songViewModel, object : SongAdapter.OnSongClickListener {
                override fun onSongClick(item: String) {
                    songViewModel.setSong(item)
                    navController.navigate(R.id.action_songListFragment_to_songDetailFragment)
                }
            })
        }

        refreshLayout.setOnRefreshListener {
            songViewModel.retrieveSongs()
            refreshLayout.isRefreshing = false
            (song_list.adapter as SongAdapter).reloadData()
        }


        song_list_create.setOnClickListener {
            navController.navigate(R.id.action_songListFragment_to_songChordsFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        (song_list.adapter as SongAdapter).reloadData()
    }
}
