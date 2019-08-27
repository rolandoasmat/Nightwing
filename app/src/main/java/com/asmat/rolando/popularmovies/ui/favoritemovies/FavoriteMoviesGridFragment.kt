package com.asmat.rolando.popularmovies.ui.favoritemovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.moviegrid.BaseMovieGridFragment

class FavoriteMoviesGridFragment : BaseMovieGridFragment() {

    override val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(FavoriteMoviesViewModel::class.java) }

}