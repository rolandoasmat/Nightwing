package com.asmat.rolando.popularmovies.model.mappers

import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailsUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class WatchLaterMovieMapper @Inject constructor() {

    fun from(data: MovieDetailsUIModel): WatchLaterMovie {
        return WatchLaterMovie(
                data.id,
                data.posterPath,
                data.overview,
                data.releaseDate,
                data.title,
                data.backdropPath,
                data.voteAverage.toDouble())
    }
}