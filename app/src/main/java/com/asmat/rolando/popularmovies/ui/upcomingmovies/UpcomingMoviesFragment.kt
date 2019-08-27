package com.asmat.rolando.popularmovies.ui.upcomingmovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridFragment

class UpcomingMoviesFragment : PaginatedMovieGridFragment() {

    override val viewModel: UpcomingMoviesViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(UpcomingMoviesViewModel::class.java)

}