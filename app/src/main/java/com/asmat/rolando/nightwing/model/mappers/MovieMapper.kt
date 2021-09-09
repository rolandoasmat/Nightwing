package com.asmat.rolando.nightwing.model.mappers

import com.asmat.rolando.nightwing.model.Movie
import com.asmat.rolando.nightwing.model.MovieSummary
import com.asmat.rolando.nightwing.networking.models.MovieDetailsResponse
import com.asmat.rolando.nightwing.networking.models.MoviesResponse
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

    fun movieResponseToMovieSummary(data: MoviesResponse.Movie): MovieSummary {
        return MovieSummary(
            id = data.id!!,
            title = data.title,
            posterPath = data.poster_path
        )
    }
}