package com.asmat.rolando.popularmovies.ui.watchlatermovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.moviegrid.BaseMovieGridFragment

class WatchLaterMoviesGridFragment : BaseMovieGridFragment() {

    override val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(WatchLaterViewModel::class.java) }


}