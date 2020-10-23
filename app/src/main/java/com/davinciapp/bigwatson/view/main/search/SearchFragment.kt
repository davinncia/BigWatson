package com.davinciapp.bigwatson.view.main.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davinciapp.bigwatson.R
import com.davinciapp.bigwatson.model.TwitterUser
import com.davinciapp.bigwatson.view.main.UserListAdapter
import com.davinciapp.bigwatson.view.tweets.TweetsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var textViewSearch: TextView
    private lateinit var editText: EditText
    private lateinit var eagleImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView =  inflater.inflate(R.layout.fragment_search, container, false)

        val viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        usersRecyclerView = rootView.findViewById(R.id.recycler_view_users_search)
        textViewSearch = rootView.findViewById(R.id.tv_twitter_search)
        editText = rootView.findViewById(R.id.et_twitter_search)
        eagleImageView = rootView.findViewById(R.id.iv_eagle_search)

        val userAdapter = UserListAdapter(listOf(), object : UserListAdapter.OnUserClickListener {
            override fun onUserClick(user: TwitterUser) {
                viewModel.selectUser(user)
                startActivity(TweetsActivity.newIntent(requireContext()))
            }
        })

        initRecyclerView(userAdapter)

        viewModel.usersLiveData.observe(viewLifecycleOwner) {
            userAdapter.setUsersList(it)
        }

        // Search edit text
        editText.addTextChangedListener(object : TextWatcher {

            var searchUi = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 2) {
                    if (!searchUi) {
                        displaySearchUi()
                        searchUi = true
                    }
                    viewModel.searchUsers(s.toString())
                } else if (count <= 2 && searchUi) {
                    searchUi = false
                    displayOriginalLayout()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        return rootView
    }

    private fun displaySearchUi() {
        textViewSearch.visibility = View.GONE
        eagleImageView.visibility = View.GONE
        usersRecyclerView.visibility = View.VISIBLE
    }

    private fun displayOriginalLayout() {
        textViewSearch.visibility = View.VISIBLE
        eagleImageView.visibility = View.VISIBLE
        usersRecyclerView.visibility = View.GONE
    }

    private fun initRecyclerView(userAdapter: UserListAdapter) {
        usersRecyclerView.apply {
            setHasFixedSize(true)

            adapter = userAdapter

            layoutManager = LinearLayoutManager(requireContext())
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}