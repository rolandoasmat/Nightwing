package com.asmat.rolando.popularmovies.ui.favoritemovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.common.*
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory

class FavoriteMoviesGridFragment : BaseMovieGridFragment() {

    override val viewModel by lazy { ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository)).get(FavoriteMoviesViewModel::class.java) }

}