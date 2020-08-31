package com.asmat.rolando.nightwing.model.mappers

import com.asmat.rolando.nightwing.database.entities.FavoriteMovie
import com.asmat.rolando.nightwing.database.entities.WatchLaterMovie
import com.asmat.rolando.nightwing.model.Movie
import com.asmat.rolando.nightwing.networking.models.MovieDetailsResponse
import com.asmat.rolando.nightwing.networking.models.MoviesResponse
import com.asmat.rolando.nightwing.ui.moviegrid.MovieGridItemUiModel
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

    open fun from(data: FavoriteMovie): MovieGridItemUiModel {
        return MovieGridItemUiModel(
                data.id,
                data.title,
                data.posterURL)
    }

    open fun from(data: WatchLaterMovie): MovieGridItemUiModel {
        return MovieGridItemUiModel(
                data.id,
                data.title,
                data.posterURL)
    }
}