package com.asmat.rolando.nightwing.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.ui.moviegrid.BaseMovieGridFragment
import com.asmat.rolando.nightwing.ui.mylists.MyListsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_saved.*

class SavedFragment: Fragment(), BaseMovieGridFragment.MovieGridCallbacks {

    private val tabName = listOf("Movies", "TV Shows")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = MyListsPagerAdapter(this)
        container?.adapter = adapter
        TabLayoutMediator(tabs, container) { tab, position ->
            tab.text = tabName[position]
        }.attach()
    }

    override fun showMovieDetailScreen(movieID: Int) {
        val action = SavedFragmentDirections.globalActionSavedTabToMovieDetailsScreen(movieID)
        findNavController().navigate(action)
    }

}