package com.davinciapp.bigwatson

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.personality_insights.v3.PersonalityInsights
import com.ibm.watson.personality_insights.v3.model.ProfileOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import twitter4j.Paging
import twitter4j.TwitterFactory
import twitter4j.User
import twitter4j.auth.AccessToken


class MainActivity : AppCompatActivity() {

    // HIDDEN

    


    private val twitter = TwitterFactory().instance


    private val textView by bind<TextView>(R.id.tv_main)
    private val editText by bind<EditText>(R.id.et_main)
    private val usersBtn by bind<Button>(R.id.btn_user_main)
    private val tweetBtn by bind<Button>(R.id.btn_tweets_main)
    private val insightBtn by bind<Button>(R.id.btn_insight_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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