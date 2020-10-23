package com.davinciapp.bigwatson.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import twitter4j.*
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwitterRepo @Inject constructor() {

    //TODO hide
    private val consumerKey = "xxx"
    private val secretConsumerKey = "xxx"
    private val oauthAccessToken = "xxx-xxx"
    private val secretOauthAccessToken = "xxx"

    private val config = ConfigurationBuilder()
        .setOAuthConsumerKey(consumerKey)
        .setOAuthConsumerSecret(secretConsumerKey)
        .setOAuthAccessToken(oauthAccessToken)
        .setOAuthAccessTokenSecret(secretOauthAccessToken)
        .setTweetModeExtended(true)
        .build()

    private val twitter = TwitterFactory(config).instance


    suspend fun searchUser(input: String): List<User> =
        try {
            twitter.searchUsers(input, 1) // 20 users per page
        } catch (e: TwitterException) {
            Log.e("exception", "Error searching user")
            listOf()
        }

    suspend fun fetchTweets(id: Long): List<Status> {

        val paging = Paging().apply { count = 100 }

        return try {
            twitter.getUserTimeline(id, paging)
        } catch (e: TwitterException) {
            Log.e("exception", "Error fetching tweets")
            listOf()
        }
    }

    fun tweet(): LiveData<String> = MutableLiveData("Tweet")
}