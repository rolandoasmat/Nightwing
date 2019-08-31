package com.asmat.rolando.popularmovies.ui.nowplayingmovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridFragment
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory

class NowPlayingMoviesFragment : PaginatedMovieGridFragment() {

    override val viewModel: NowPlayingMoviesViewModel
        get() = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository, dataModelMapper, uiModelMapper)).get(NowPlayingMoviesViewModel::class.java)

}