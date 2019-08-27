package com.asmat.rolando.popularmovies.ui.topratedmovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridFragment

class TopRatedMoviesFragment : PaginatedMovieGridFragment() {

    override val viewModel: TopRatedMoviesViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(TopRatedMoviesViewModel::class.java)

}