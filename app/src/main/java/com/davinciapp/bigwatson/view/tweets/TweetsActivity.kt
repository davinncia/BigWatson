package com.davinciapp.bigwatson.view.tweets

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.davinciapp.bigwatson.R
import com.davinciapp.bigwatson.view.analysis.AnalysisActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TweetsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweets)

        val analyseBtn = findViewById<Button>(R.id.btn_analyse_tweets)

        // View Model
        val viewModel = ViewModelProvider(this).get(TweetsViewModel::class.java)
        viewModel.tweetsLiveData.observe(this,  Observer {
            initRecyclerView(it)
        })

        // User header
        viewModel.userLiveData.observe(this,  Observer {
            findViewById<TextView>(R.id.tv_user_name_profile_header).text = it.displayName
            findViewById<ImageView>(R.id.iv_verified_badge_profile_header).apply {
                visibility = if (it.isVerified) View.VISIBLE
                else View.GONE
            }
            Glide.with(this).load(it.imageUrl).circleCrop().into(
                findViewById(R.id.iv_profile_header)
            )
        })

        // Words count
        viewModel.wordsCountLiveData.observe(this, Observer {
            findViewById<TextView>(R.id.tv_words_count_tweets).text = "$it words collected."
        })

        // Button
        viewModel.enoughDataLiveData.observe(this, Observer {
            if (it) {
                analyseBtn.apply {
                    hint = "Analyse tweets"
                    setOnClickListener {
                        startActivity(AnalysisActivity.newIntent(this@TweetsActivity, false))
                    }
                }
            } else {
                analyseBtn.apply {
                    hint = "Not enough data to proceed with analysis"
                    alpha = 0.8F
                }
            }
        })
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