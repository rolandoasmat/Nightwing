package com.asmat.rolando.popularmovies.ui.watchlatermovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.common.*
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory

class WatchLaterMoviesGridFragment : BaseMovieGridFragment() {

    override val viewModel: BaseMovieGridViewModel
        get() = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository)).get(WatchLaterViewModel::class.java)


}