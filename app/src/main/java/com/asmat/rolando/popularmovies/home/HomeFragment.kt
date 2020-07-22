package com.asmat.rolando.popularmovies.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.discover.DiscoverPagerAdapter
import com.asmat.rolando.popularmovies.ui.moviegrid.BaseMovieGridFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: Fragment(), BaseMovieGridFragment.MovieGridCallbacks {

    private val tabName = listOf("Popular", "Top Rated", "Now Playing", "Upcoming")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Movie Night"
    }

    private fun setupViewPager() {
        val adapter = DiscoverPagerAdapter(this)
        container?.adapter = adapter
        TabLayoutMediator(tabs, container) { tab, position ->
            tab.text = tabName[position]
        }.attach()
    }

    override fun showMovieDetailScreen(movieID: Int) {
        val action = HomeFragmentDirections.actionGlobalActionToMovieDetailsScreen(movieID)
        findNavController().navigate(action)
    }

}