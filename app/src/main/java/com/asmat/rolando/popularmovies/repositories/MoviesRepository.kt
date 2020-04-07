package com.asmat.rolando.popularmovies.repositories

import androidx.lifecycle.LiveData

import com.asmat.rolando.popularmovies.database.DatabaseManager
import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie
import com.asmat.rolando.popularmovies.model.*
import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.*
import io.reactivex.Scheduler
import io.reactivex.Single

/**
 * Used by ViewModels to access movie related data sources
 */
class MoviesRepository(private val db: DatabaseManager,
                       private val tmdbClient: TheMovieDBClient,
                       private val backgroundScheduler: Scheduler,
                       private val mainThreadScheduler: Scheduler) {

    /**
     * DB
     */

    // Favorite Movie
    fun getFavoriteMovie(movieID: Int): LiveData<FavoriteMovie> {
        return db.getFavoriteMovie(movieID)
    }

    fun removeFavoriteMovie(movieID: Int) {
        db.deleteFavoriteMovie(movieID).subscribe({}, {})
    }

    fun insertFavoriteMovie(movie: FavoriteMovie) {
        db.addFavoriteMovie(movie).subscribe({}, {})
    }

    fun getAllFavoriteMovies(): LiveData<List<FavoriteMovie>> {
        return db.getAllFavoriteMovies()
    }

    // Watch Later Movie
    fun getWatchLaterMovie(movieID: Int): LiveData<WatchLaterMovie> {
        return db.getWatchLaterMovie(movieID)
    }

    fun removeWatchLaterMovie(movieID: Int) {
        db.deleteWatchLaterMovie(movieID).subscribe({}, {})
    }

    fun insertWatchLaterMovie(movie: WatchLaterMovie) {
        db.addWatchLaterMovie(movie).subscribe({}, {})
    }

    fun getAllWatchLaterMovies(): LiveData<List<WatchLaterMovie>> {
        return db.getAllWatchLaterMovies()
    }

    /**
     * Network
     */

    val popularMoviesPaginatedRequest = PopularMoviesPaginatedRequest(tmdbClient, backgroundScheduler, mainThreadScheduler)
    val topRatedPaginatedRequest = TopRatedPaginatedRequest(tmdbClient, backgroundScheduler, mainThreadScheduler)
    val nowPlayingPaginatedRequest = NowPlayingPaginatedRequest(tmdbClient, backgroundScheduler, mainThreadScheduler)
    val upcomingPaginatedRequest = UpcomingPaginatedRequest(tmdbClient, backgroundScheduler, mainThreadScheduler)
    val searchMoviesPaginatedRequest = SearchMoviesPaginatedRequest(tmdbClient, backgroundScheduler, mainThreadScheduler)

    fun getMovieVideos(movieID: Int): Single<VideosResponse> {
        return tmdbClient.getMovieVideos(movieID).subscribeOn(backgroundScheduler)
    }

    fun getMovieReviews(movieID: Int): Single<ReviewsResponse> {
        return tmdbClient.getMovieReviews(movieID).subscribeOn(backgroundScheduler)
    }

    fun getMovieCredits(movieID: Int): Single<CreditsResponse> {
        return tmdbClient.getMovieCredits(movieID).subscribeOn(backgroundScheduler)
    }
}