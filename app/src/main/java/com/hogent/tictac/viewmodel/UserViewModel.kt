package com.hogent.tictac.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hogent.tictac.persistence.Model
import com.hogent.tictac.persistence.network.UserApiService
import com.hogent.tictac.persistence.room.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Credentials
import javax.inject.Inject

class UserViewModel : InjectedViewModel() {

    var currentUser = MutableLiveData<Model.User>()
    var databaseUser: LiveData<Model.User> = userRepository.user

    private lateinit var subscription: Disposable

    @Inject
    lateinit var userApiService: UserApiService

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var context: Context

    fun login(loginDetails: Model.Login) {
        setToken(Credentials.basic(loginDetails.name, loginDetails.password))
        subscription = userApiService.login()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    onRetrieve(user)
                },
                { error ->
                    Log.d("LOGIN", "$error")
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
                    Log.d("REGISTER", "$error")
                }
            )
    }

    private fun setToken(token: String) {
        val preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        preferences.edit().putString("token", token).apply()
    }

    private fun onRetrieve(user: Model.User) {
        val userToken = Credentials.basic(user.name, user.password)
        setToken(userToken)
        currentUser.value = user
        userRepository.insert(user)
    }
}