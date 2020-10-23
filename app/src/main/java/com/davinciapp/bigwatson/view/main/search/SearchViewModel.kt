package com.davinciapp.bigwatson.view.main.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinciapp.bigwatson.repository.InMemoryRepo
import com.davinciapp.bigwatson.repository.TwitterRepo
import com.davinciapp.bigwatson.model.TwitterUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchViewModel @ViewModelInject constructor(
    private val twitterRepo : TwitterRepo,
    private val inMemoryRepo: InMemoryRepo,
) : ViewModel() {

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