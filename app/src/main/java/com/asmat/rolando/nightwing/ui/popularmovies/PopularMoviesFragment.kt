package com.asmat.rolando.nightwing.ui.popularmovies

import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.nightwing.ui.moviegrid.paginated.PaginatedMovieGridFragment
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory

class PopularMoviesFragment: PaginatedMovieGridFragment() {

    override val viewModel by lazy { ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository, dataModelMapper, uiModelMapper, deepLinksUtils)).get(PopularMoviesViewModel::class.java) }

    override fun goToMovieDetailsScreen(movieID: Int) {
        val action = PopularMoviesFragmentDirections.actionPopularMoviesGridToMovieDetailsScreen(movieID)
        findNavController().navigate(action)
    }

}