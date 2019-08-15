package com.hogent.tictac.viewmodel

import android.content.Context
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

/**
 *  ViewModel for users
 *
 *  Used for handling the current user
 *
 *  @property currentUser the currently logged in user
 *  @property databaseUser the user currently saved in the room database
 *  @property userToast LiveData variable used to display toast messages, is observed in MainActivity
 *  @property userRepository repository used for managing the user in the local room database
 *  @property userApiService service used for registration, logging in and retrieving users
 */
class UserViewModel : InjectedViewModel() {

    var currentUser = MutableLiveData<Model.User>()
    var databaseUser: LiveData<Model.User> = userRepository.user
    var userToast = MutableLiveData<String>()

    private lateinit var subscription: Disposable

    @Inject
    lateinit var userApiService: UserApiService

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var context: Context

    /**
     * Logs the user in a username and a password
     *
     * @param loginDetails the username and password to use
     */
    fun login(loginDetails: Model.Login) {
        val credentials = Credentials.basic(loginDetails.name, loginDetails.password)
        subscription = userApiService.login(credentials)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    onRetrieve(user)
                    setToken(credentials)
                    userToast.value = "Login successful!"
                },
                {
                    userToast.value = "Given user/password doesn't exist"
                }
            )
    }

    /**
     * Registers a user using a username and a password
     *
     * @param registerDetails the username and password to use
     */
    fun register(registerDetails: Model.Register) {
        val credentials = Credentials.basic(registerDetails.name, registerDetails.password)
        subscription = userApiService.register(registerDetails)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    onRetrieve(user)
                    setToken(credentials)
                    userToast.value = "Registration successful!"
                },
                {
                    userToast.value = "Registration failed."
                }
            )
    }

    private fun setToken(token: String) {
        val preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        preferences.edit().putString("token", token).apply()
    }

    /**
     * Updates current user data
     *
     * @param user the new user
     */
    private fun onRetrieve(user: Model.User) {
        currentUser.value = user
        userRepository.insert(user)
    }
}