package com.asmat.rolando.popularmovies.ui.popularmovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.common.BaseMovieGridFragment
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory

class PopularMoviesFragment : BaseMovieGridFragment() {

    override val viewModel: PopularMoviesViewModel
        get() = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository)).get(PopularMoviesViewModel::class.java)

}