package com.davinciapp.bigwatson.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwitterRepo @Inject constructor() {

    fun tweet(): LiveData<String> = MutableLiveData("Tweet")
}