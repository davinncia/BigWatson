package com.davinciapp.bigwatson.view.tweets

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.davinciapp.bigwatson.R
import com.davinciapp.bigwatson.view.main.TwitterUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TweetsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweets)

        val viewModel = ViewModelProvider(this).get(TweetsViewModel::class.java)
        viewModel.tweetsLiveData.observe(this) {
            initRecyclerView(it)
        }

        val user = intent.getParcelableExtra<TwitterUser>(USER_KEY)

        user?.let {
            viewModel.fetchTweets(it.id)

            findViewById<TextView>(R.id.tv_user_name_tweets).text = it.displayName
            Glide.with(this).load(it.imageUrl).circleCrop().into(
                findViewById(R.id.iv_profile_tweets)
            )
        }
    }

    private fun initRecyclerView(tweets: List<TweetUi>) {
        findViewById<RecyclerView>(R.id.recycler_view_tweets).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@TweetsActivity)
            adapter = TweetAdapter(tweets)
        }
    }

    companion object {
        const val USER_KEY = "user_key"

        fun newIntent(context: Context, user: TwitterUser) : Intent {
            val intent = Intent(context, TweetsActivity::class.java)
            intent.putExtra(USER_KEY, user)
            return intent
        }
    }
}