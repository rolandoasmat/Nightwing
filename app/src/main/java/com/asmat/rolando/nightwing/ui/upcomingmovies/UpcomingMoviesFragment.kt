package com.asmat.rolando.nightwing.ui.upcomingmovies

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.nightwing.ui.moviegrid.paginated.PaginatedMovieGridFragment

class UpcomingMoviesFragment: PaginatedMovieGridFragment() {

    override val viewModel: UpcomingMoviesViewModel by viewModels { viewModelFactory }

    override fun goToMovieDetailsScreen(movieID: Int) {
        val action = UpcomingMoviesFragmentDirections.actionUpcomingMoviesGridToMovieDetailsScreen(movieID)
        findNavController().navigate(action)
    }

}