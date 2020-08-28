package com.asmat.rolando.nightwing.ui.popularmovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.nightwing.ui.moviegrid.paginated.PaginatedMovieGridFragment
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory

class PopularMoviesFragment: PaginatedMovieGridFragment() {

    override val viewModel by lazy { ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository, dataModelMapper, uiModelMapper, deepLinksUtils)).get(PopularMoviesViewModel::class.java) }

}