package com.asmat.rolando.popularmovies.model.mappers

import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MovieDetailsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailsUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataModelMapper @Inject constructor(private val movieMapper: MovieMapper,
                                          private val favoriteMovieMapper: FavoriteMovieMapper,
                                          private val watchLaterMovieMapper: WatchLaterMovieMapper) {

    fun map(data: List<MoviesResponse.Movie>?) = data?.map { movieMapper.from(it) }

    fun mapToFavoriteMovie(data: MovieDetailsResponse) = favoriteMovieMapper.from(data)

    fun mapFavoriteMovies(data: List<FavoriteMovie>) = data.map { movieMapper.from(it) }

    fun mapToWatchLaterMovie(data: MovieDetailsResponse) = watchLaterMovieMapper.from(data)

    fun mapWatchLaterMovies(data: List<WatchLaterMovie>) = data.map { movieMapper.from(it) }

}