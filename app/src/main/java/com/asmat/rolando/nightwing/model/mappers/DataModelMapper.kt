package com.asmat.rolando.nightwing.model.mappers

import com.asmat.rolando.nightwing.database.entities.FavoriteMovie
import com.asmat.rolando.nightwing.database.entities.WatchLaterMovie
import com.asmat.rolando.nightwing.movie_details.MovieDetailsUIModel
import com.asmat.rolando.nightwing.networking.models.MoviesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataModelMapper @Inject constructor(private val movieMapper: MovieMapper,
                                          private val favoriteMovieMapper: FavoriteMovieMapper,
                                          private val watchLaterMovieMapper: WatchLaterMovieMapper) {

    fun map(data: List<MoviesResponse.Movie>?) = data?.map { movieMapper.from(it) }

    fun mapToFavoriteMovie(data: MovieDetailsUIModel) = favoriteMovieMapper.from(data)

    fun mapFavoriteMovies(data: List<FavoriteMovie>) = data.map { movieMapper.from(it) }

    fun mapToWatchLaterMovie(data: MovieDetailsUIModel) = watchLaterMovieMapper.from(data)

    fun mapWatchLaterMovies(data: List<WatchLaterMovie>) = data.map { movieMapper.from(it) }

}