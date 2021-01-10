package com.asmat.rolando.nightwing.tv_shows_tab.top_rated

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.ui.grid.GridFragment
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import javax.inject.Inject

class TopRatedTvShowsFragment: GridFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override val viewModel: TopRatedTvShowsViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as NightwingApplication).component().inject(this)
    }

    override fun onItemClicked(id: Int) {
        val action = TopRatedTvShowsFragmentDirections.actionTopRatedTvShowsToTvShowDetailsFragment(id)
        findNavController().navigate(action)
    }
}