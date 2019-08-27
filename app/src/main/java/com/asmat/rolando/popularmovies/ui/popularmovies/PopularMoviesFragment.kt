package com.asmat.rolando.popularmovies.ui.popularmovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridFragment

class PopularMoviesFragment : PaginatedMovieGridFragment() {

    override val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(PopularMoviesViewModel::class.java) }

}