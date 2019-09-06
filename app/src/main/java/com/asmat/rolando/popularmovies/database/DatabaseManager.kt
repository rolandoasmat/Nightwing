package com.asmat.rolando.popularmovies.database

import androidx.lifecycle.LiveData
import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single

/**
 * Database CRUD operations
 */
open class DatabaseManager(private val dao: MoviesDAO,
                           private val backgroundScheduler: Scheduler) {

    /**
     * Favorite movies
     */

    open fun addFavoriteMovie(favoriteMovie: FavoriteMovie): Completable {
        return dao.insertFavoriteMovie(favoriteMovie).subscribeOn(backgroundScheduler)
    }

    open fun getFavoriteMovie(id: Int): LiveData<FavoriteMovie> {
        return dao.findFavoriteMovie(id)
    }

    open fun deleteFavoriteMovie(id: Int): Single<Int> {
        return dao.deleteFavoriteMovie(id).subscribeOn(backgroundScheduler)
    }

    open fun getAllFavoriteMovies(): LiveData<List<FavoriteMovie>> {
        return dao.loadAllFavoriteMovies()
    }

    /**
     * Watch Later movies
     */

    open fun addWatchLaterMovie(watchLaterMovie: WatchLaterMovie): Completable {
        return dao.insertWatchLaterMovie(watchLaterMovie).subscribeOn(backgroundScheduler)
    }

    open fun getWatchLaterMovie(id: Int): LiveData<WatchLaterMovie> {
        return dao.findWatchLaterMovie(id)
    }

    open fun deleteWatchLaterMovie(id: Int): Single<Int> {
        return dao.deleteWatchLaterMovie(id).subscribeOn(backgroundScheduler)
    }

    open fun getAllWatchLaterMovies(): LiveData<List<WatchLaterMovie>> {
        return dao.loadAllWatchLaterMovies()
    }

}