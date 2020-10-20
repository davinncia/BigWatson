package com.davinciapp.bigwatson.view.tweets

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.davinciapp.bigwatson.repository.InMemoryRepo
import com.davinciapp.bigwatson.repository.TwitterRepo
import kotlinx.coroutines.Dispatchers

class TweetsViewModel @ViewModelInject constructor(
    private val twitterRepo: TwitterRepo,
    inMemoryRepo: InMemoryRepo,
    private val dateUtils: DateUtils
) : ViewModel() {

    val userLiveData = inMemoryRepo.userSelected

    val tweetsLiveData = Transformations.switchMap(userLiveData) { user ->

        //liveData builder to run asynchronous task and emit the result
        liveData<List<TweetUi>>(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            val uiTweets = arrayListOf<TweetUi>()

            twitterRepo.fetchTweets(user.id).map {
                val uiTweet = TweetUi(
                    dateUtils.getDateString(it.createdAt),
                    it.text
                )
                uiTweets.add(uiTweet)
            }
            emit(uiTweets)
        }
    }

    //TODO save text

}