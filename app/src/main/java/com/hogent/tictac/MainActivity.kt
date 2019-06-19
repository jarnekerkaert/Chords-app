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
import java.lang.IllegalStateException

class MainActivity : AppCompatActivity(), SongChordsFragment.OnSongChordsFragmentInteractionListener {

    private lateinit var songViewModel: SongViewModel
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        songViewModel = ViewModelProviders.of(this).get(SongViewModel::class.java)
        mediaPlayer = MediaPlayer()

        setContentView(R.layout.activity_main)
        setSupportActionBar(app_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSongChordsFragmentInteraction(chord: String) {
        songViewModel.songSelected.value?.chords?.add(Note.valueOf(chord))

        val chordId = resources.getIdentifier(chord.toLowerCase(), "raw", packageName)
        if (chordId != 0) {
            if(mediaPlayer.isPlaying)
                mediaPlayer.stop()
            mediaPlayer = MediaPlayer.create(this, chordId)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mp -> mp.release() }
        }

        Toast.makeText(this, "Added $chord to song", Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}
