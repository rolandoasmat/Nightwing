package com.asmat.rolando.popularmovies.database

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie

/**
 * Database CRUD operations
 */
open class DatabaseManager(private val dao: MoviesDAO) {

    /**
     * Favorite movies
     */

    fun addFavoriteMovie(favoriteMovie: FavoriteMovie) {
        dao.insertFavoriteMovie(favoriteMovie)
    }

    open fun getFavoriteMovie(id: Int): LiveData<FavoriteMovie> {
        return dao.findFavoriteMovie(id)
    }

    fun deleteFavoriteMovie(id: Int) {
        dao.deleteFavoriteMovie(id)
    }

    fun getAllFavoriteMovies(): LiveData<List<FavoriteMovie>> {
        return dao.loadAllFavoriteMovies()
    }

    /**
     * Watch Later movies
     */

    fun addWatchLaterMovie(watchLaterMovie: WatchLaterMovie) {
        dao.insertWatchLaterMovie(watchLaterMovie)
    }

    fun getWatchLaterMovie(id: Int): LiveData<WatchLaterMovie> {
        return dao.findWatchLaterMovie(id)
    }

    fun deleteWatchLaterMovie(id: Int) {
        dao.deleteWatchLaterMovie(id)
    }

    fun getAllWatchLaterMovies(): LiveData<List<WatchLaterMovie>> {
        return dao.loadAllWatchLaterMovies()
    }

}