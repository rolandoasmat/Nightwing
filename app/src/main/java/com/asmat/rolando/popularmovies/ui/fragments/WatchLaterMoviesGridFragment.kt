package com.asmat.rolando.popularmovies.ui.fragments

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.asmat.rolando.popularmovies.ui.adapters.grid.WatchLaterMoviesGridAdapter
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.model.mappers.MovieMapper
import com.asmat.rolando.popularmovies.ui.adapters.MovieAdapterOnClickHandler
import com.asmat.rolando.popularmovies.ui.adapters.grid.BaseMoviesGridAdapter

class WatchLaterMoviesGridFragment : BaseGridFragment() {

    override val movieSource: LiveData<List<Movie>>
        get() {
            val source = moviesRepository.getAllWatchLaterMovies()
            return Transformations.map(source) { watchLaterMovies ->
                watchLaterMovies.map {
                    MovieMapper.from(it)
                }
            }
        }

    override fun getAdapter(handler: MovieAdapterOnClickHandler): BaseMoviesGridAdapter {
        return WatchLaterMoviesGridAdapter(handler)
    }
}