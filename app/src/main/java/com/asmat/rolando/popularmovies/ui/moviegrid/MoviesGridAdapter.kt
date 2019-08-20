package com.asmat.rolando.popularmovies.ui.moviegrid

import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.ui.moviegrid.BaseMoviesGridAdapter

class MoviesGridAdapter(callback: Callback?) : BaseMoviesGridAdapter(callback) {

    override val emptyStateLayoutID: Int
        get() = R.layout.empty_state_generic

}