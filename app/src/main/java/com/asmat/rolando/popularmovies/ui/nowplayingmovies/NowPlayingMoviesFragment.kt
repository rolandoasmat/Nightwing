package com.asmat.rolando.popularmovies.ui.nowplayingmovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridFragment

class NowPlayingMoviesFragment : PaginatedMovieGridFragment() {

    override val viewModel: NowPlayingMoviesViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(NowPlayingMoviesViewModel::class.java)

}