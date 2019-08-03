package com.asmat.rolando.popularmovies.ui.common

import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.common.MovieAdapterOnClickHandler
import com.asmat.rolando.popularmovies.ui.common.BaseMoviesGridAdapter

class MoviesGridAdapter(callback: Callback?) : BaseMoviesGridAdapter(callback) {

    override val emptyStateLayoutID: Int
        get() = R.layout.empty_state_generic

}