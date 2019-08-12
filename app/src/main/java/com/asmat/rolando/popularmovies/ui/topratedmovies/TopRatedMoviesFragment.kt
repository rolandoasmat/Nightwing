package com.asmat.rolando.popularmovies.ui.topratedmovies

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.common.MovieGridFragment
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory

class TopRatedMoviesFragment : MovieGridFragment() {

    override val viewModel: TopRatedMoviesViewModel
        get() = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository)).get(TopRatedMoviesViewModel::class.java)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.movies.observe(this, Observer { movies ->
            moviesGridAdapter?.setMovies(movies)
        })
    }
}