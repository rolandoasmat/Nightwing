package com.asmat.rolando.popularmovies.ui.upcomingmovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridFragment
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory

class UpcomingMoviesFragment(callbacks: MovieGridCallbacks) : PaginatedMovieGridFragment(callbacks) {

    override val viewModel: UpcomingMoviesViewModel
        get() = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository, dataModelMapper, uiModelMapper)).get(UpcomingMoviesViewModel::class.java)

}