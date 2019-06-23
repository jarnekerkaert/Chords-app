package com.hogent.tictac.viewmodel

import android.content.Context
import android.util.Base64
import androidx.lifecycle.MutableLiveData
import com.hogent.tictac.persistence.Model
import com.hogent.tictac.persistence.database.UserApiService
import com.hogent.tictac.persistence.room.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Credentials
import javax.inject.Inject

class UserViewModel : InjectedViewModel() {

    var currentUser = MutableLiveData<Model.User>()

    private lateinit var subscription: Disposable

    @Inject
    lateinit var userApiService: UserApiService

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var context: Context

    fun login(loginDetails: Model.Login) {
        subscription = userApiService.login(loginDetails, Credentials.basic(loginDetails.name, loginDetails.password))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    onRetrieve(user)
                },
                { error ->

                }
            )
    }

    fun register(registerDetails: Model.Register) {
        subscription = userApiService.register(registerDetails)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    onRetrieve(user)
                },
                { error ->

                }
            )
    }

    private fun onRetrieve(user: Model.User) {
        val userToken = Credentials.basic(user.name, user.password)
        val preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        preferences.edit().putString("token", userToken.toString()).apply()
        currentUser.value = user
    }
}