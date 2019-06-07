package com.hogent.tictac.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hogent.tictac.common.Model

@Dao
interface SongDao {

    @Query("SELECT * FROM song")
    fun getAllSongs(): LiveData<List<Model.Song>>

    @Insert
    fun insert(song: Model.Song)

    @Query("DELETE FROM song")
    fun deleteAll()
}