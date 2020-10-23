package com.davinciapp.bigwatson.view.main.favorites

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinciapp.bigwatson.model.TwitterUser
import com.davinciapp.bigwatson.repository.InMemoryRepo
import com.davinciapp.bigwatson.repository.UserRepo
import kotlinx.coroutines.launch

class FavoritesViewModel @ViewModelInject constructor(
    private val inMemoryRepo: InMemoryRepo,
    private val userRepo: UserRepo
) : ViewModel() {

    val usersLiveData = userRepo.getAll()
    val showEmptyView = Transformations.map(usersLiveData) { it.isEmpty() }

    fun selectUser(user: TwitterUser) {
        inMemoryRepo.selectUser(user)
    }

    fun deleteUser(user: TwitterUser) {
        viewModelScope.launch {
            userRepo.delete(user)
            //TODO : delete analyse that goes with
        }
    }
}