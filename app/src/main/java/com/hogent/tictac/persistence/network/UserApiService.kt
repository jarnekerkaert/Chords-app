package com.hogent.tictac.persistence.network

import com.hogent.tictac.persistence.Model
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApiService {

    @GET("/login")
    fun login(@Header("Authorization") header: String): Observable<Model.User>

    @POST("/register")
    fun register(@Body response: Model.Register): Observable<Model.User>
}