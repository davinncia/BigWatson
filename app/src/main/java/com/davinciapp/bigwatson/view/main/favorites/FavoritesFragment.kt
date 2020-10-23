package com.davinciapp.bigwatson.view.main.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davinciapp.bigwatson.R
import com.davinciapp.bigwatson.model.TwitterUser
import com.davinciapp.bigwatson.view.analysis.AnalysisActivity
import com.davinciapp.bigwatson.view.main.UserListAdapter
import com.davinciapp.bigwatson.view.tweets.TweetsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private lateinit var viewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_favorites, container, false)

        viewModel = ViewModelProvider(this)[FavoritesViewModel::class.java]

        val userAdapter = UserListAdapter(listOf(), object : UserListAdapter.OnUserClickListener {
            override fun onUserClick(user: TwitterUser) {
                viewModel.selectUser(user)
                startActivity(AnalysisActivity.newIntent(requireContext(), true))
            }
        })

        initRecyclerView(userAdapter, rootView)

        viewModel.usersLiveData.observe(viewLifecycleOwner) {
            userAdapter.setUsersList(it)
        }

        viewModel.showEmptyView.observe(viewLifecycleOwner) {
            rootView.findViewById<TextView>(R.id.tv_empty_view_fav).apply {
                visibility = if (it) View.VISIBLE
                else View.GONE
            }
        }

        return rootView
    }

    private fun initRecyclerView(userAdapter: UserListAdapter, rootView: View) {
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recycler_view_users_fav)

        recyclerView.apply {
            setHasFixedSize(true)
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val swipeToDeleteCallback = SwipeToDeleteCallback(object : SwipeToDeleteCallback.OnSwipeToDeleteListener {
            override fun onSwipeToDelete(position: Int) {
                viewModel.deleteUser(userAdapter.getItem(position))
            }
        })

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoritesFragment()
    }
}