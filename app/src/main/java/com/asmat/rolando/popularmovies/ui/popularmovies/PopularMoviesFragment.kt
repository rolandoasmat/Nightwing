package com.asmat.rolando.popularmovies.ui.popularmovies

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.common.MovieGridFragment
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory

class PopularMoviesFragment : MovieGridFragment() {

    override val viewModel: PopularMoviesViewModel
        get() = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository)).get(PopularMoviesViewModel::class.java)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load()
    }
}