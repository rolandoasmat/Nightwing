package com.asmat.rolando.popularmovies.managers

import android.arch.lifecycle.LiveData

import com.asmat.rolando.popularmovies.database.DatabaseManager
import com.asmat.rolando.popularmovies.database.FavoriteMovie
import com.asmat.rolando.popularmovies.database.Movie
import com.asmat.rolando.popularmovies.database.WatchLaterMovie
import com.asmat.rolando.popularmovies.models.Cast
import com.asmat.rolando.popularmovies.models.Review
import com.asmat.rolando.popularmovies.models.Video
import com.asmat.rolando.popularmovies.networking.TheMovieDBClient
import io.reactivex.Single

object MoviesRepository {

    fun getMovie(movieID: Int): Single<Movie> {
        return TheMovieDBClient.getMovieDetails(movieID)
    }

    fun getFavoriteMovie(movieID: Int): LiveData<FavoriteMovie> {
        return DatabaseManager.INSTANCE.getFavoriteMovie(movieID)
    }

    fun getWatchLaterMovie(movieID: Int): LiveData<WatchLaterMovie> {
        return DatabaseManager.INSTANCE.getWatchLaterMovie(movieID)
    }

    fun getVideos(movieID: Int): Single<List<Video>> {
        return TheMovieDBClient.getMovieVideos(movieID)
    }

    fun getReviews(movieID: Int): Single<List<Review>> {
        return TheMovieDBClient.getMovieReviews(movieID)
    }

    fun getMovieCreadits(movieID: Int): Single<List<Cast>> {
        return TheMovieDBClient.getMovieCredits(movieID)
    }
}