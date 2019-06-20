package com.hogent.tictac.persistence.database

import com.hogent.tictac.persistence.Model
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("/API/users/login")
    fun login(@Body response: Model.Login): Observable<Model.User>

    @POST("/API/users/register")
    fun register(@Body response: Model.Register): Observable<Model.User>
}