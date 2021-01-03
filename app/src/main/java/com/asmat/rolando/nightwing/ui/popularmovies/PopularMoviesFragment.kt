package com.asmat.rolando.nightwing.ui.popularmovies

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.nightwing.ui.moviegrid.paginated.PaginatedMovieGridFragment

class PopularMoviesFragment: PaginatedMovieGridFragment() {

    override val viewModel: PopularMoviesViewModel by viewModels { viewModelFactory }

    override fun goToMovieDetailsScreen(movieID: Int) {
        val action = PopularMoviesFragmentDirections.actionPopularMoviesGridToMovieDetailsScreen(movieID)
        findNavController().navigate(action)
    }

}