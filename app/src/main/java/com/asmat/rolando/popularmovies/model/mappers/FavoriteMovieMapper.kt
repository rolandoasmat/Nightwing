package com.asmat.rolando.popularmovies.model.mappers

import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.model.Movie

object FavoriteMovieMapper {

    fun from(data: Movie): FavoriteMovie {
        return FavoriteMovie(
                data.id,
                data.posterPath,
                data.overview,
                data.releaseDate,
                data.title,
                data.backdropPath,
                data.voteAverage)
    }
}