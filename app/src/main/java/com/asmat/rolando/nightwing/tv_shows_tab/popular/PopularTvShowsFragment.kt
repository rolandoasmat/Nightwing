package com.asmat.rolando.nightwing.tv_shows_tab.popular

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.nightwing.HomeDirections
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.home_tab.HomeTabFragmentDirections
import com.asmat.rolando.nightwing.ui.grid.GridFragment
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import javax.inject.Inject

class PopularTvShowsFragment: GridFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override val viewModel: PopularTvShowsViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as NightwingApplication).component().inject(this)
    }

    override fun onItemClicked(id: Int) {
        val action = HomeTabFragmentDirections.actionGlobalActionToTvShowDetailsScreen(id)
        findNavController().navigate(action)
    }
}