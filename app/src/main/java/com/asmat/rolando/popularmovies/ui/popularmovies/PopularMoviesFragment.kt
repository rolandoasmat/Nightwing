package com.asmat.rolando.popularmovies.ui.popularmovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridFragment
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory

class PopularMoviesFragment(callbacks: MovieGridCallbacks) : PaginatedMovieGridFragment(callbacks) {

    override val viewModel by lazy { ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository, dataModelMapper, uiModelMapper, deepLinksUtils)).get(PopularMoviesViewModel::class.java) }

}