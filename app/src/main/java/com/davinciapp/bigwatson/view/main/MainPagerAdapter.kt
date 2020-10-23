package com.davinciapp.bigwatson.view.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.davinciapp.bigwatson.view.main.favorites.FavoritesFragment
import com.davinciapp.bigwatson.view.main.search.SearchFragment

class MainPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {

    override fun getCount(): Int = NBR_PAGES

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> SearchFragment.newInstance()
            else -> return FavoritesFragment.newInstance()
        }
    }

    companion object {
        private const val NBR_PAGES = 2
    }

}