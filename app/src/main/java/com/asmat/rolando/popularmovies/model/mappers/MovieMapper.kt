package com.asmat.rolando.popularmovies.model.mappers

import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.networking.models.MovieDetailsResponse
import com.asmat.rolando.popularmovies.networking.models.MoviesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class MovieMapper @Inject constructor() {

    open fun from(data: MovieDetailsResponse): Movie {
        return Movie(
                data.poster_path,
                data.overview ?: "",
                data.release_date ?: "",
                data.id ?: 0,
                data.title ?: "",
                data.backdrop_path,
                data.vote_average ?: 0.0)
    }

    open fun from(data: MoviesResponse.Movie): Movie {
        return Movie(
                data.poster_path,
                data.overview ?: "",
                data.release_date ?: "",
                data.id ?: 0,
                data.title ?: "",
                data.backdrop_path,
                data.vote_average ?: 0.0)
    }

    open fun from(data: FavoriteMovie): Movie {
        return Movie(
                data.posterPath,
                data.overview,
                data.releaseDate,
                data.id,
                data.title,
                data.backdropPath,
                data.voteAverage)
    }

    open fun from(data: WatchLaterMovie): Movie {
        return Movie(
                data.posterPath,
                data.overview,
                data.releaseDate,
                data.id,
                data.title,
                data.backdropPath,
                data.voteAverage)
    }
}