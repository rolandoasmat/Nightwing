package com.asmat.rolando.popularmovies.repositories

import androidx.lifecycle.LiveData
import android.os.AsyncTask

import com.asmat.rolando.popularmovies.database.DatabaseManager
import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie
import com.asmat.rolando.popularmovies.model.*
import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Used by ViewModels to access movie related data sources
 */
class MoviesRepository(private val db: DatabaseManager,
                       private val tmdbClient: TheMovieDBClient) {

    private var movieDetailsData: Movie? = null

    /**
     * Cache
     */

    fun setMovieDetailsData(data: Movie) {
        movieDetailsData = data
    }

    fun getMovieDetailsData() = movieDetailsData

    /**
     * DB
     */

    // Favorite Movie
    fun getFavoriteMovie(movieID: Int): LiveData<FavoriteMovie> {
        return db.getFavoriteMovie(movieID)
    }

    fun removeFavoriteMovie(movieID: Int) {
        runOnBackground { db.deleteFavoriteMovie(movieID) }
    }

    fun insertFavoriteMovie(movie: FavoriteMovie) {
        runOnBackground { db.addFavoriteMovie(movie) }
    }

    fun getAllFavoriteMovies(): LiveData<List<FavoriteMovie>> {
        return db.getAllFavoriteMovies()
    }

    // Watch Later Movie
    fun getWatchLaterMovie(movieID: Int): LiveData<WatchLaterMovie> {
        return db.getWatchLaterMovie(movieID)
    }

    fun removeWatchLaterMovie(movieID: Int) {
        runOnBackground { db.deleteWatchLaterMovie(movieID) }
    }

    fun insertWatchLaterMovie(movie: WatchLaterMovie) {
        runOnBackground { db.addWatchLaterMovie(movie) }
    }

    fun getAllWatchLaterMovies(): LiveData<List<WatchLaterMovie>> {
        return db.getAllWatchLaterMovies()
    }

    /**
     * Network
     */

    val popularMoviesPaginatedRequest = PopularMoviesPaginatedRequest(tmdbClient)
    val topRatedPaginatedRequest = TopRatedPaginatedRequest(tmdbClient)
    val nowPlayingPaginatedRequest = NowPlayingPaginatedRequest(tmdbClient)
    val upcomingPaginatedRequest = UpcomingPaginatedRequest(tmdbClient)
    val searchMoviesPaginatedRequest = SearchMoviesPaginatedRequest(tmdbClient)

    fun getMovieDetails(movieID: Int): Single<MovieDetailsResponse> {
        return tmdbClient.getMovieDetails(movieID).subscribeOn(Schedulers.io())
    }

    fun getMovieVideos(movieID: Int): Single<VideosResponse> {
        return tmdbClient.getMovieVideos(movieID).subscribeOn(Schedulers.io())
    }

    fun getMovieReviews(movieID: Int): Single<ReviewsResponse> {
        return tmdbClient.getMovieReviews(movieID).subscribeOn(Schedulers.io())
    }

    fun getMovieCredits(movieID: Int): Single<CreditsResponse> {
        return tmdbClient.getMovieCredits(movieID).subscribeOn(Schedulers.io())
    }

    fun searchMovies(searchTerm: String, page: Int): Single<MoviesResponse> {
        return tmdbClient.searchMovie(searchTerm, page).subscribeOn(Schedulers.io())
    }

    /**
     * Private
     */

    private fun runOnBackground(closure: () -> Unit?) {
        AsyncTask.execute { closure.invoke() }
    }
}