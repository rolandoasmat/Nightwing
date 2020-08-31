package com.asmat.rolando.nightwing.model.mappers

import com.asmat.rolando.nightwing.database.entities.WatchLaterMovie
import com.asmat.rolando.nightwing.movie_details.MovieDetailsUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class WatchLaterMovieMapper @Inject constructor() {

    fun from(data: MovieDetailsUIModel): WatchLaterMovie {
        return WatchLaterMovie(
                data.id,
                data.posterURL,
                data.releaseDate,
                data.title)
    }
}