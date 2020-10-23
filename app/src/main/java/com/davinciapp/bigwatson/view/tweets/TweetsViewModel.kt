package com.davinciapp.bigwatson.view.tweets

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.davinciapp.bigwatson.repository.InMemoryRepo
import com.davinciapp.bigwatson.repository.TwitterRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
            var plainText = ""

            twitterRepo.fetchTweets(user.id).map {

                val uiTweet = TweetUi(
                    dateUtils.getDateString(it.createdAt),
                    it.text
                )
                uiTweets.add(uiTweet)

                plainText += it.text
            }
            emit(uiTweets)

            withContext(Dispatchers.Main) {
                inMemoryRepo.setTextToAnalyse(plainText)
            }
        }
    }

    val wordsCountLiveData = Transformations.map(tweetsLiveData) { uiTweets ->
        var wordsCount = 0
        uiTweets.map {
            wordsCount += it.text.split("\\s".toRegex()).size
        }
        wordsCount
    }

    val enoughDataLiveData = Transformations.map(wordsCountLiveData) {
        it > 100
    }

}