package com.davinciapp.bigwatson

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davinciapp.bigwatson.repository.TwitterRepo

class MainViewModel @ViewModelInject constructor(
    private val twitterRepo : TwitterRepo
) : ViewModel() {

    val test = twitterRepo.tweet()
}