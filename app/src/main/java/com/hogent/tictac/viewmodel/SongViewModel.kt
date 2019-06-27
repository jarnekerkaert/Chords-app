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

class SongViewModel : InjectedViewModel() {
    var songSelected = MutableLiveData<Model.Song>()
    var songs = MutableLiveData<Array<Model.Song>>()
    var songToast = MutableLiveData<String>()

    private lateinit var subscribe: Disposable

    @Inject
    lateinit var songRepository: SongRepository

    @Inject
    lateinit var songApiService: SongApiService

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

    fun setSong(id: String) {
        songSelected.value = null
        subscribe = songApiService.findSongById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> songSelected.value = result },
                { error -> songToast.value = "Song not found" }
            )
    }

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