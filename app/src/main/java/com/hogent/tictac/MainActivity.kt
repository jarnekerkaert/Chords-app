package com.hogent.tictac

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var songListFragment: SongListFragment
    private lateinit var createSongFragment: CreateSongFragment
    private lateinit var songDetailFragment: SongDetailFragment
    private lateinit var songViewModel: SongViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        songViewModel = ViewModelProviders.of(this).get(SongViewModel::class.java)
        songViewModel.createSongEvent.observe(this, Observer { navigateSongDetail() })

        setContentView(R.layout.activity_main)
        setSupportActionBar(app_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

//        app_back.setOnClickListener {
//            onBackPressed()
//        }

        navigateSongList()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStack()
    }

    fun navigateSongList() {
        songListFragment = SongListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.app_main_container, songListFragment)
            .addToBackStack(null)
            .commit()
    }

    fun navigateCreateSong() {
        createSongFragment = CreateSongFragment()
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.app_main_container, createSongFragment)
            .addToBackStack(null)
            .commit()
    }

    fun navigateSongDetail() {
        songDetailFragment = SongDetailFragment()
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.app_main_container, songDetailFragment)
            .addToBackStack(null)
            .commit()
    }
}
