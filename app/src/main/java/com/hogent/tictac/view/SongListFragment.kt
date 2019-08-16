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
import com.hogent.tictac.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_song_list.*

/**
 * Fragment for displaying all songs
 *
 * This fragment acts as a home page
 *
 * @property userViewModel viewModel for getting user data
 * @property songViewModel viewModel for setting created song data
 * @property navController navigation controller for navigating to other fragments
 */
class SongListFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var navController: NavController
    private lateinit var userViewModel: UserViewModel

    /**
     * Initializes viewmodels, navigation controller and asks songViewModel to retrieve all songs in the database.
     *
     * Also adjusts actionbar accordingly
     */
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_song_list, container, false)

        songViewModel = activity?.run {
            ViewModelProviders.of(this).get(SongViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        userViewModel = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        songViewModel.retrieveSongs()
        navController = this.findNavController()

        val actionBar: ActionBar? = (activity as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setDisplayShowHomeEnabled(false)

        return view
    }

    /**
     * Initialization of recycler view, refresh listener and create song button.
     *
     * When a song in the recycler view is clicked, the song is set in songViewModel and the navigation controller tries
     * to navigate to songDetailFragment.
     *
     * When the refresh action is activated (swipe down), the data in the recycler view is reloaded.
     *
     * When the create song button is clicked, check if a user is logged in. If true, navigation controller will navigate
     * to songChordsFragment, otherwise a toast message is displayed.
     */
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
            if (userViewModel.databaseUser.value != null)
                navController.navigate(R.id.action_songListFragment_to_songChordsFragment)
            else
                songViewModel.songToast.value = "You are not logged in"
        }
    }

    /**
     * When navigated back to this fragment, the data in the recycler view is reloaded to reflect recent changes.
     */
    override fun onResume() {
        super.onResume()
        (song_list.adapter as SongAdapter).reloadData()
    }
}
