package com.asmat.rolando.nightwing.ui.topratedmovies

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.nightwing.ui.moviegrid.paginated.PaginatedMovieGridFragment

class TopRatedMoviesFragment: PaginatedMovieGridFragment() {

    override val viewModel: TopRatedMoviesViewModel by viewModels { viewModelFactory }
    override fun goToMovieDetailsScreen(movieID: Int) {
        val action = TopRatedMoviesFragmentDirections.actionTopRatedMoviesGridToMovieDetailsScreen(movieID)
        findNavController().navigate(action)
    }
}