package com.asmat.rolando.nightwing.ui.recommended_movies

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.nightwing.ui.moviegrid.paginated.PaginatedMovieGridFragment
import com.asmat.rolando.nightwing.ui.popularmovies.PopularMoviesFragmentDirections

class RecommendedMoviesFragment: PaginatedMovieGridFragment() {

    override val viewModel: RecommendedMoviesViewModel by viewModels { viewModelFactory }

    override fun goToMovieDetailsScreen(movieID: Int) {
        val action = PopularMoviesFragmentDirections.actionPopularMoviesGridToMovieDetailsScreen(movieID)
        findNavController().navigate(action)
    }

}