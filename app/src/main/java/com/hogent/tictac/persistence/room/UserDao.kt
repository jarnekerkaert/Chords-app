package com.hogent.tictac.persistence.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.hogent.tictac.persistence.Model

@Dao
interface UserDao {
    @Query("SELECT * from user ORDER BY id LIMIT 1")
    fun getUser(): LiveData<Model.User>

    @Insert(onConflict = REPLACE)
    fun insert(user: Model.User)

    @Query("DELETE FROM user")
    fun nukeUsers()

    @Update(onConflict = REPLACE)
    fun updateUser(user: Model.User)
}