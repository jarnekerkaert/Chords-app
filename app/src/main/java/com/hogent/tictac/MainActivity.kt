package com.hogent.tictac

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.hogent.tictac.common.Note
import com.hogent.tictac.view.SongViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SongChordsFragment.OnSongChordsFragmentInteractionListener {

    private lateinit var songViewModel: SongViewModel
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        songViewModel = ViewModelProviders.of(this).get(SongViewModel::class.java)

        setContentView(R.layout.activity_main)
        setSupportActionBar(app_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSongChordsFragmentInteraction(chord: String) {
        songViewModel.songCreating.value?.chords?.add(Note.valueOf(chord))

        val mediaPlayer: MediaPlayer? = MediaPlayer.create(this, R.raw.)
        mediaPlayer?.start()

        Toast.makeText(this, "Added $chord to song", Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}
