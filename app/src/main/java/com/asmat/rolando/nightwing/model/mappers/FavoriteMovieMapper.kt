package com.asmat.rolando.nightwing.model.mappers

import com.asmat.rolando.nightwing.database.entities.FavoriteMovie
import com.asmat.rolando.nightwing.movie_details.MovieDetailsUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class FavoriteMovieMapper @Inject constructor() {

    fun from(data: MovieDetailsUIModel): FavoriteMovie {
        return FavoriteMovie(
                data.id,
                data.posterURL,
                data.releaseDate,
                data.title)
    }
}