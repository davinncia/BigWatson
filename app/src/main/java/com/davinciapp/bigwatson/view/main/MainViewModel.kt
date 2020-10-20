package com.davinciapp.bigwatson.view.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinciapp.bigwatson.repository.InMemoryRepo
import com.davinciapp.bigwatson.repository.TwitterRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel @ViewModelInject constructor(
    private val twitterRepo : TwitterRepo,
    private val inMemoryRepo: InMemoryRepo
) : ViewModel() {

    val test = twitterRepo.tweet()

    private val users = MutableLiveData<List<TwitterUser>>()
    val usersLiveData: LiveData<List<TwitterUser>> = users

    fun searchUsers(input: String) {
        viewModelScope.launch(Dispatchers.IO) {
                 val result = twitterRepo.searchUser(input).map {
                    TwitterUser(it.id, it.biggerProfileImageURL, it.screenName, it.isVerified)
                }

            withContext(Dispatchers.Main) {
                users.value = result
            }
        }
    }

    fun selectUser(user: TwitterUser) {
        inMemoryRepo.selectUser(user)
    }
}