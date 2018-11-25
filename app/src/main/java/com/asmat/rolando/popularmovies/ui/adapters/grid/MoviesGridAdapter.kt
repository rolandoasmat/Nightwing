package com.asmat.rolando.popularmovies.ui.adapters.grid

import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.adapters.MovieAdapterOnClickHandler

class MoviesGridAdapter(handler: MovieAdapterOnClickHandler) : BaseMoviesGridAdapter(handler) {

    override val emptyStateLayoutID: Int
        get() = R.layout.empty_state_generic

}