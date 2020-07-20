package com.asmat.rolando.popularmovies.ui.nowplayingmovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridFragment
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory

class NowPlayingMoviesFragment(callbacks: MovieGridCallbacks) : PaginatedMovieGridFragment(callbacks) {

    override val viewModel: NowPlayingMoviesViewModel
        get() = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository, dataModelMapper, uiModelMapper, deepLinksUtils)).get(NowPlayingMoviesViewModel::class.java)

}