package com.asmat.rolando.popularmovies.ui.nowplayingmovies

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.common.MovieGridFragment
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory

class NowPlayingMoviesFragment : MovieGridFragment() {

    override val viewModel: NowPlayingMoviesViewModel
        get() = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository)).get(NowPlayingMoviesViewModel::class.java)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load()
    }
}