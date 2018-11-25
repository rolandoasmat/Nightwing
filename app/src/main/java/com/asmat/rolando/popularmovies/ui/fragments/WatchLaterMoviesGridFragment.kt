package com.asmat.rolando.popularmovies.ui.fragments

import android.arch.lifecycle.LiveData
import com.asmat.rolando.popularmovies.ui.adapters.grid.MoviesGridBaseAdapter
import com.asmat.rolando.popularmovies.ui.adapters.grid.WatchLaterMoviesGridAdapter
import com.asmat.rolando.popularmovies.database.DatabaseManager
import com.asmat.rolando.popularmovies.ui.adapters.MovieAdapterOnClickHandler

class WatchLaterMoviesGridFragment : BaseGridFragment() {

    internal val movieSource: LiveData<Array<Movie>>
        get() = DatabaseManager.INSTANCE.getWatchLaterMovies()

    internal fun getAdapter(handler: MovieAdapterOnClickHandler): MoviesGridBaseAdapter {
        return WatchLaterMoviesGridAdapter(handler)
    }
}