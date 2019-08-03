package com.asmat.rolando.popularmovies.repositories

import androidx.lifecycle.LiveData
import android.os.AsyncTask

import com.asmat.rolando.popularmovies.database.DatabaseManager
import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.*
import io.reactivex.Single

/**
 * Used by ViewModels to access movie related data sources
 */
class MoviesRepository(private val db: DatabaseManager,
                       private val tmdbClient: TheMovieDBClient) {

    private val movies = mutableListOf<Movie>()

    /**
     * Cache
     */
    fun getPopularMovieAt(index: Int): Movie {
        return movies[index]
    }

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

    fun getPopularMovies(page: Int) : Single<MoviesResponse> {
        return tmdbClient.getPopularMovies(page)
    }

    fun getTopRatedMovies(page: Int) : Single<MoviesResponse> {
        return tmdbClient.getTopRatedMovies(page)
    }

    fun getNowPlayingMovies(page: Int) : Single<MoviesResponse> {
        return tmdbClient.getNowPlayingMovies(page)
    }

    fun getComingSoonMovies(page: Int) : Single<MoviesResponse> {
        return tmdbClient.getUpcomingMovies(page)
    }

    fun getMovieDetails(movieID: Int): Single<MovieDetailsResponse> {
        return tmdbClient.getMovieDetails(movieID)
    }

    fun getMovieVideos(movieID: Int): Single<VideosResponse> {
        return tmdbClient.getMovieVideos(movieID)
    }

    fun getMovieReviews(movieID: Int): Single<ReviewsResponse> {
        return tmdbClient.getMovieReviews(movieID)
    }

    fun getMovieCredits(movieID: Int): Single<CreditsResponse> {
        return tmdbClient.getMovieCredits(movieID)
    }

    fun searchMovies(searchTerm: String, page: Int): Single<MoviesResponse> {
        return tmdbClient.searchMovie(searchTerm, page)
    }

    /**
     * Private
     */

    private fun runOnBackground(closure: () -> Unit?) {
        AsyncTask.execute { closure.invoke() }
    }
}