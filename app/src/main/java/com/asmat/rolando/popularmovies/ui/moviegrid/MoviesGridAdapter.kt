package com.asmat.rolando.popularmovies.ui.moviegrid

import com.asmat.rolando.popularmovies.R

class MoviesGridAdapter(callback: Callback?) : BaseMoviesGridAdapter(callback) {

    override val emptyStateLayoutID: Int = R.layout.empty_state_generic // TODO update empty state

}