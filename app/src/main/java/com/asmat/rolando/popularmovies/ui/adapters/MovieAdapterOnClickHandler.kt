package com.asmat.rolando.popularmovies.ui.adapters

import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse

interface MovieAdapterOnClickHandler {
    fun onClick(movie: MoviesResponse.Movie)
}
