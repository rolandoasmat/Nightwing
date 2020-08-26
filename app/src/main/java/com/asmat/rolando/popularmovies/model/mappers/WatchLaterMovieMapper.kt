package com.asmat.rolando.popularmovies.model.mappers

import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie
import com.asmat.rolando.popularmovies.networking.models.MovieDetailsResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class WatchLaterMovieMapper @Inject constructor() {

    fun from(data: MovieDetailsResponse): WatchLaterMovie {
        return WatchLaterMovie(
                data.id ?: 0,
                data.poster_path,
                data.overview ?: "",
                data.release_date ?: "",
                data.title ?: "",
                data.backdrop_path,
                data.vote_average ?: 0.0)
    }
}