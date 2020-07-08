package com.asmat.rolando.popularmovies.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.discover.DiscoverPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: Fragment() {

    private val tabName = listOf("Popular", "Top Rated", "Now Playing", "Upcoming")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = DiscoverPagerAdapter(this)
        container?.adapter = adapter
        TabLayoutMediator(tabs, container) { tab, position ->
            tab.text = tabName[position]
        }.attach()
    }

}