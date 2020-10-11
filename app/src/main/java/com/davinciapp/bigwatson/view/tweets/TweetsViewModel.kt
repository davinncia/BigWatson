package com.davinciapp.bigwatson.view.tweets

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinciapp.bigwatson.repository.TwitterRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TweetsViewModel @ViewModelInject constructor(
    private val twitterRepo: TwitterRepo,
    private val dateUtils: DateUtils
) : ViewModel() {

    private val tweets = MutableLiveData<List<TweetUi>>()
    val tweetsLiveData = tweets

    fun fetchTweets(id: Long) {

        viewModelScope.launch(Dispatchers.IO) {

            val uiTweets = arrayListOf<TweetUi>()
            twitterRepo.fetchTweets(id).map {
                val uiTweet = TweetUi(
                    dateUtils.getDateString(it.createdAt),
                    it.text
                )
                uiTweets.add(uiTweet)
            }
            withContext(Dispatchers.Main) {
                tweets.value = uiTweets
            }
        }
    }
}