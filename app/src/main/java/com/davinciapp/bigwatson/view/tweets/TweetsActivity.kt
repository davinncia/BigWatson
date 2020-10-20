package com.davinciapp.bigwatson.view.tweets

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.davinciapp.bigwatson.R
import com.davinciapp.bigwatson.view.analysis.AnalysisActivity
import com.davinciapp.bigwatson.view.main.TwitterUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TweetsActivity : AppCompatActivity() {

    //TODO words count / actualize button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweets)

        val viewModel = ViewModelProvider(this).get(TweetsViewModel::class.java)

        viewModel.tweetsLiveData.observe(this,  Observer {
            initRecyclerView(it)
        })

        viewModel.userLiveData.observe(this,  Observer {
            findViewById<TextView>(R.id.tv_user_name_profile_header).text = it.displayName
            Glide.with(this).load(it.imageUrl).circleCrop().into(
                findViewById(R.id.iv_profile_header)
            )
        })

        findViewById<Button>(R.id.btn_analyse_tweets).setOnClickListener {
            startActivity(AnalysisActivity.newIntent(this))
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
        fun newIntent(context: Context) =  Intent(context, TweetsActivity::class.java)
    }
}