package com.davinciapp.bigwatson

import android.app.Application
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.personality_insights.v3.PersonalityInsights
import com.ibm.watson.personality_insights.v3.model.ProfileOptions
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import twitter4j.Paging
import twitter4j.TwitterFactory
import twitter4j.User
import twitter4j.auth.AccessToken

// Generate Hilt component
@HiltAndroidApp
class MainApplication: Application()

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    //TODO hide
    private val consumerKey = "e8QECMaHpAoBfdTuuHmr7vUuy"
    private val secretConsumerKey = "DYFQLZ7v5bTL5hZlSOuhtnbqfJVfzi5fYB6XAdnbtQuyQF6TUN"
    private val oauthAccessToken = "1115593275832823808-DqH70AdQGEyActQRdZX0W7wiSU9zvE"
    private val secretOauthAccessToken = "16QC7b5iZKGhaea9jYqbJyad9PmGB8QhRnKgyc3GhSVoJ"

    private val watsonApiKey = "LBAEiPZ69mfa3X3lxIge4GiPKNctHS8n152kenKsATze"
    private val watsonUrl = "https://gateway-lon.watsonplatform.net/personality-insights/api"


    private val twitter = TwitterFactory().instance

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val textView by bind<TextView>(R.id.tv_main)
    private val editText by bind<EditText>(R.id.et_main)
    private val usersBtn by bind<Button>(R.id.btn_user_main)
    private val tweetBtn by bind<Button>(R.id.btn_tweets_main)
    private val insightBtn by bind<Button>(R.id.btn_insight_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //DEBUG
        viewModel.test.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        twitter.setOAuthConsumer(consumerKey, secretConsumerKey)
        twitter.oAuthAccessToken = AccessToken(oauthAccessToken, secretOauthAccessToken)

        usersBtn.setOnClickListener {
            if (!editText.text.isNullOrBlank()) {
                searchUsers(editText.text.toString())
            }
        }

        tweetBtn.setOnClickListener {
            if (!editText.text.isNullOrBlank()) {
                fetchTweets(editText.text.toString())
            }
        }

        insightBtn.setOnClickListener {
            if (textView.text.length > 100) {
                runPersonalityInsight(textView.text.toString())
            } else Toast.makeText(this, "Not enough data to analyse", Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchUsers(input: String) {

        GlobalScope.launch {
            val result: List<User> = twitter.searchUsers(input, 1) // 20 users per page

            var usersStr = ""
            for (r in result) usersStr += "${r.screenName}\n"

            runOnUiThread {
                textView.text = usersStr
            }
        }
    }

    private fun fetchTweets(userName: String) {

        GlobalScope.launch {
            val tweets = twitter.getUserTimeline(userName, Paging(2)) // 40 tweets

            var tweetsText = ""
            tweets.forEach { tweetsText += "${it.text}\n" }

            runOnUiThread {
                textView.text = tweetsText
            }
        }
    }

    private fun runPersonalityInsight(text: String) {
        val authenticator = IamAuthenticator(watsonApiKey)
        val personalityInsight = PersonalityInsights("2017-10-13", authenticator)
        personalityInsight.serviceUrl = watsonUrl

        val profileOptions = ProfileOptions.Builder()
            .text(text)
            //.consumptionPreferences(true)
            //.rawScores(true)
            .build()


        GlobalScope.launch {
            val profile = personalityInsight.profile(profileOptions).execute()

            runOnUiThread {
                textView.text = profile.result.toString()
            }
        }

    }
}