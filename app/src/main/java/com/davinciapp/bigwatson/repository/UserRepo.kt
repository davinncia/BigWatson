package com.davinciapp.bigwatson.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.davinciapp.bigwatson.AppDatabase
import com.davinciapp.bigwatson.dao.UserDao
import com.davinciapp.bigwatson.model.TwitterUser
import javax.inject.Inject

class UserRepo @Inject constructor(context: Context) {

    private val userDao: UserDao

    init {
        val db = AppDatabase.getDatabase(context)
        userDao = db.userDao()
    }

    suspend fun insert(user: TwitterUser) = userDao.insert(user)

    suspend fun delete(user: TwitterUser) = userDao.delete(user)

    fun getAll(): LiveData<List<TwitterUser>> = userDao.getAll()

    suspend fun getAllId(): List<Long> = userDao.getAllId()
}