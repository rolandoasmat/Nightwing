package com.asmat.rolando.popularmovies.ui.watchlatermovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.moviegrid.BaseMovieGridFragment
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory

class WatchLaterMoviesGridFragment(callbacks: MovieGridCallbacks) : BaseMovieGridFragment(callbacks) {

    override val viewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository, dataModelMapper, uiModelMapper, deepLinksUtils)).get(WatchLaterViewModel::class.java) }

}