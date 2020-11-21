package com.asmat.rolando.nightwing.ui.nowplayingmovies

import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.nightwing.ui.moviegrid.paginated.PaginatedMovieGridFragment
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory

class NowPlayingMoviesFragment: PaginatedMovieGridFragment() {

    override val viewModel: NowPlayingMoviesViewModel
        get() = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository, dataModelMapper, uiModelMapper, deepLinksUtils)).get(NowPlayingMoviesViewModel::class.java)

    override fun goToMovieDetailsScreen(movieID: Int) {
        val action = NowPlayingMoviesFragmentDirections.actionNowPlayingMoviesGridToMovieDetailsScreen(movieID)
        findNavController().navigate(action)
    }

}