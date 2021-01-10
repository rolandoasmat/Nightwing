package com.asmat.rolando.nightwing.tv_shows_tab.on_the_air

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.ui.grid.GridFragment
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import javax.inject.Inject

class OnTheAirTvShowsFragment: GridFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override val viewModel: OnTheAirTvShowsViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as NightwingApplication).component().inject(this)
    }

    override fun onItemClicked(id: Int) {
        val action = OnTheAirTvShowsFragmentDirections.actionOnTheAirTvShowsToTvShowDetailsFragment(id)
        findNavController().navigate(action)
    }
}