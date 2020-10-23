package com.davinciapp.bigwatson.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.davinciapp.bigwatson.R
import com.davinciapp.bigwatson.model.TwitterUser

class UserListAdapter(
    private var users: List<TwitterUser>,
    private val listener: OnUserClickListener
)
    : RecyclerView.Adapter<UserListAdapter.UserListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserListViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun getItem(position: Int) = users[position]

    fun setUsersList(list: List<TwitterUser>) {
        users = list
        notifyDataSetChanged()
    }

    inner class UserListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView = itemView.findViewById<ImageView>(R.id.iv_user_item)
        private val nameTextView = itemView.findViewById<TextView>(R.id.tv_name_user_item)
        private val badgeView = itemView.findViewById<ImageView>(R.id.iv_verified_badge_user_item)

        init {
            itemView.setOnClickListener {
                listener.onUserClick(users[adapterPosition])
            }
        }

        fun bind(user: TwitterUser) {
            Glide.with(imageView.context).load(user.imageUrl).circleCrop().into(imageView)
            nameTextView.text = user.displayName
            if (user.isVerified) badgeView.setImageResource(R.drawable.ic_twitter_verified_badge)
        }
    }

    interface OnUserClickListener {
        fun onUserClick(user: TwitterUser)
    }
}

