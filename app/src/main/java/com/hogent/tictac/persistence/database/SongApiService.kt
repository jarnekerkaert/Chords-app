package com.hogent.tictac.persistence.database

import com.hogent.tictac.persistence.Model
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SongApiService {

    @GET("/songs")
    fun findAll(): Observable<Array<Model.Song>>

    @GET("/songs/{id}")
    fun findSongById(@Path("id") id: String): Observable<Model.Song>

    @POST("/songs")
    fun addSong(@Body song: Model.Song): Observable<Model.Song>
}