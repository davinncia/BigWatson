package com.davinciapp.bigwatson.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.davinciapp.bigwatson.TwitterUser
import twitter4j.TwitterFactory
import twitter4j.User
import twitter4j.auth.AccessToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwitterRepo @Inject constructor() {

    //TODO hide
    private val consumerKey = "e8QECMaHpAoBfdTuuHmr7vUuy"
    private val secretConsumerKey = "DYFQLZ7v5bTL5hZlSOuhtnbqfJVfzi5fYB6XAdnbtQuyQF6TUN"
    private val oauthAccessToken = "1115593275832823808-DqH70AdQGEyActQRdZX0W7wiSU9zvE"
    private val secretOauthAccessToken = "16QC7b5iZKGhaea9jYqbJyad9PmGB8QhRnKgyc3GhSVoJ"

    private val twitter = TwitterFactory().instance

    init {
        twitter.setOAuthConsumer(consumerKey, secretConsumerKey)
        twitter.oAuthAccessToken = AccessToken(oauthAccessToken, secretOauthAccessToken)
    }

    suspend fun searchUser(input: String): List<User> = twitter.searchUsers(input, 1) // 20 users per page

    fun tweet(): LiveData<String> = MutableLiveData("Tweet")
}