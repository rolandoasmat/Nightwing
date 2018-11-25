package com.asmat.rolando.popularmovies.ui.adapters.grid

import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.adapters.MovieAdapterOnClickHandler

class WatchLaterMoviesGridAdapter(handler: MovieAdapterOnClickHandler) : MoviesGridBaseAdapter(handler) {

    override val emptyStateLayoutID: Int
        get() = R.layout.empty_state_watch_later

}