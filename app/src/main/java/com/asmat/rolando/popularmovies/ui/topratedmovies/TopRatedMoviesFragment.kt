package com.asmat.rolando.popularmovies.ui.topratedmovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridFragment
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory

class TopRatedMoviesFragment : PaginatedMovieGridFragment() {

    override val viewModel: TopRatedMoviesViewModel
        get() = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository, dataModelMapper, uiModelMapper)).get(TopRatedMoviesViewModel::class.java)

}