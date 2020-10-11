package com.davinciapp.bigwatson.view.tweets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.davinciapp.bigwatson.R

class TweetAdapter(val tweets: List<TweetUi>) : RecyclerView.Adapter<TweetAdapter.TweetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tweet, parent, false)
        return TweetViewHolder(view)
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        holder.bind(tweets[position])
    }

    override fun getItemCount(): Int = tweets.size

    inner class TweetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val dateView = itemView.findViewById<TextView>(R.id.tv_date_item_tweet)
        private val textView = itemView.findViewById<TextView>(R.id.tv_tweet_item_tweet)

        fun bind(tweet: TweetUi) {
            dateView.text = tweet.date
            textView.text = tweet.text
        }
    }
}