package com.davinciapp.bigwatson.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.davinciapp.bigwatson.model.TwitterUser

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: TwitterUser)

    @Delete
    suspend fun delete(user: TwitterUser)

    @Query("SELECT * FROM user_table")
    fun getAll(): LiveData<List<TwitterUser>>

    @Query("SELECT id FROM user_table")
    fun getAllId(): List<Long>
}