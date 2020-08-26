package com.asmat.rolando.popularmovies.model.mappers

import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.networking.models.MovieDetailsResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class FavoriteMovieMapper @Inject constructor() {

    fun from(data: MovieDetailsResponse): FavoriteMovie {
        return FavoriteMovie(
                data.id ?: 0,
                data.poster_path,
                data.overview ?: "",
                data.release_date ?: "",
                data.title ?: "",
                data.backdrop_path ?: "",
                data.vote_average ?: 0.0)
    }
}