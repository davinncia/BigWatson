package com.davinciapp.bigwatson.view.main

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.davinciapp.bigwatson.R
import com.davinciapp.bigwatson.view.analysis.AnalysisActivity
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

// Generate Hilt component
@HiltAndroidApp
class MainApplication: Application()

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //DEBUG
        //startActivity(AnalysisActivity.newIntent(this, false))

        val pager = findViewById<ViewPager>(R.id.view_pager_main)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout_indicator_main)

        val pagerAdapter = MainPagerAdapter(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        pager.adapter = pagerAdapter

        tabLayout.setupWithViewPager(pager)
        tabLayout.getTabAt(0)?.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_search, null)
        tabLayout.getTabAt(1)?.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_people, null)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.icon?.alpha = 255
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.icon?.alpha = 80
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

}