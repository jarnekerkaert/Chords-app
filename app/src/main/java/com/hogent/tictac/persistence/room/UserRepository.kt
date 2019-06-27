package com.hogent.tictac.persistence.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.hogent.tictac.persistence.Model
import org.jetbrains.anko.doAsync

class UserRepository(private val userDao: UserDao) {
    var user: LiveData<Model.User> = userDao.getUser()

    @WorkerThread
    fun insert(user: Model.User) {
        doAsync {
            userDao.insert(user)
        }
    }

    @WorkerThread
    fun nukeUsers() {
        doAsync {
            userDao.nukeUsers()
        }
    }

    @WorkerThread
    fun updateUser(user: Model.User) {
        doAsync {
            userDao.updateUser(user)
        }
    }
}