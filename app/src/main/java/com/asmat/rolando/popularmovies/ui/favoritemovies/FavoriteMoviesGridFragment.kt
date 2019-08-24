package com.asmat.rolando.popularmovies.ui.favoritemovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.moviegrid.BaseMovieGridFragment
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory

class FavoriteMoviesGridFragment : BaseMovieGridFragment() {

    override val requiresInternet = false

    override val viewModel by lazy { ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository)).get(FavoriteMoviesViewModel::class.java) }

}