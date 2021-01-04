package com.asmat.rolando.nightwing.tv_shows_tab.popular

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.asmat.rolando.nightwing.NightwingApplication
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
        // TODO navigate to TV show details
    }
}