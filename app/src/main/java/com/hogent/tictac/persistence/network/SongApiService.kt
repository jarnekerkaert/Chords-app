package com.hogent.tictac.persistence.network

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

    @POST("/user/{id}")
    fun addSong(@Path("id") id: String, @Body song: Model.Song): Observable<Model.Song>
}