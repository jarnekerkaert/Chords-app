package com.hogent.tictac.persistence.database

import com.hogent.tictac.persistence.Model
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApiService {

    @POST("/login")
    fun login(@Body response: Model.Login, @Header("Authorization") header: String): Observable<Model.User>

    @POST("/register")
    fun register(@Body response: Model.Register): Observable<Model.User>
}