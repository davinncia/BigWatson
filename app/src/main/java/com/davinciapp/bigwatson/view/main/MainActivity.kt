package com.davinciapp.bigwatson.view.main

import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davinciapp.bigwatson.R
import com.davinciapp.bigwatson.bind
import com.davinciapp.bigwatson.view.analysis.AnalysisActivity
import com.davinciapp.bigwatson.view.tweets.TweetsActivity
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.personality_insights.v3.PersonalityInsights
import com.ibm.watson.personality_insights.v3.model.ProfileOptions
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// Generate Hilt component
@HiltAndroidApp
class MainApplication: Application()

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val usersRecyclerView by bind<RecyclerView>(R.id.recycler_view_users_main)
    private val textViewSearch by bind<TextView>(R.id.tv_twitter_search_main)
    private val editText by bind<EditText>(R.id.et_twitter_search_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //DEBUG
        startActivity(AnalysisActivity.newIntent(this))

        val userAdapter = UserListAdapter(listOf(), object : UserListAdapter.OnUserClickListener {
            override fun onUserClick(user: TwitterUser) {
                viewModel.selectUser(user)
                startActivity(TweetsActivity.newIntent(this@MainActivity))
            }
        })

        initRecyclerView(userAdapter)

        //DEBUG
        viewModel.test.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        viewModel.usersLiveData.observe(this,  Observer {
            userAdapter.setUsersList(it)
        })

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
    }

    private fun displaySearchUi() {
        textViewSearch.visibility = View.GONE
        iv_eagle_main.visibility = View.GONE
        usersRecyclerView.visibility = View.VISIBLE
    }

    private fun displayOriginalLayout() {
        textViewSearch.visibility = View.VISIBLE
        iv_eagle_main.visibility = View.VISIBLE
        usersRecyclerView.visibility = View.GONE
    }

    private fun initRecyclerView(userAdapter: UserListAdapter) {
        usersRecyclerView.apply {
            setHasFixedSize(true)

            adapter = userAdapter

            layoutManager = LinearLayoutManager(this@MainActivity)
        }

    }
}