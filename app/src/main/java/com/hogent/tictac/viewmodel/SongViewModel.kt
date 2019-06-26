package com.hogent.tictac.viewmodel

import android.util.Log
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
                    Log.d("SONG_LIST", "$error")
                }
            )
    }

    fun setSong(id: String) {
        subscribe = songApiService.findSongById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> songSelected.value = result },
                { error -> Log.d("SAVE", error.toString()) }
            )
    }

    fun saveSong(userId: String) {
        songApiService.addSong(userId, songSelected.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Log.d("SAVE", "SUCCESS")
                },
                { error ->
                    Log.d("SAVE", error.toString())
                }
            )
    }
}