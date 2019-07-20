package com.asmat.rolando.popularmovies.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.asmat.rolando.popularmovies.ui.mylists.WatchLaterMoviesGridAdapter
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.model.mappers.MovieMapper
import com.asmat.rolando.popularmovies.ui.common.MovieAdapterOnClickHandler
import com.asmat.rolando.popularmovies.ui.common.BaseGridFragment
import com.asmat.rolando.popularmovies.ui.common.BaseMoviesGridAdapter

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