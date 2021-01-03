package com.asmat.rolando.nightwing.ui.nowplayingmovies

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.nightwing.ui.moviegrid.paginated.PaginatedMovieGridFragment

class NowPlayingMoviesFragment: PaginatedMovieGridFragment() {

    override val viewModel: NowPlayingMoviesViewModel by viewModels { viewModelFactory }

    override fun goToMovieDetailsScreen(movieID: Int) {
        val action = NowPlayingMoviesFragmentDirections.actionNowPlayingMoviesGridToMovieDetailsScreen(movieID)
        findNavController().navigate(action)
    }

}