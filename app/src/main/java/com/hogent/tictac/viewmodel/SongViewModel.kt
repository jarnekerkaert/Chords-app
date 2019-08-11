package com.hogent.tictac.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.hogent.tictac.persistence.Model
import com.hogent.tictac.persistence.network.SongApiService
import com.hogent.tictac.persistence.room.SongRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 *  ViewModel for songs
 *
 *  Used by anonymous users and registered users, manages all songs in the database and selected songs
 *
 *  @property songSelected the currently selected song
 *  @property songs all songs in the system
 *  @property songToast LiveData variable used to display toast messages, is observed in MainActivity
 *  @property songRepository repository used for managing songs in the local room database
 *  @property songApiService service used for managing songs in the postgres database
 */
class SongViewModel : InjectedViewModel() {
    var songSelected = MutableLiveData<Model.Song>()
    var songs = MutableLiveData<Array<Model.Song>>()
    var songToast = MutableLiveData<String>()

    private lateinit var subscribe: Disposable

    @Inject
    lateinit var songRepository: SongRepository

    @Inject
    lateinit var songApiService: SongApiService

    /**
     * Retrieves all songs in the postgres database
     */
    fun retrieveSongs() {
        subscribe = songApiService.findAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    songs.postValue(result)
                },
                { error ->
                    songToast.value = "Error retrieving songs"
                }
            )
    }

    /**
     * Retrieves the song with the given id and sets it as the selected song
     *
     * @param songId the given song id
     */
    fun setSong(songId: String) {
        songSelected.value = null
        subscribe = songApiService.findSongById(songId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> songSelected.value = result },
                { error -> songToast.value = "Song not found" }
            )
    }

    /**
     * Saves the selected song to the postgres database under a given user
     *
     * @param userId the given user id
     */
    @SuppressLint("CheckResult")
    fun saveSong(userId: String) {
        songApiService.addSong(userId, songSelected.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    songToast.value = "Song saved as ${result.title}!"
                },
                { error ->
                    songToast.value = "Failed to save song"
                }
            )
    }
}