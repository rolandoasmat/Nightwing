package com.asmat.rolando.popularmovies.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Room
import android.content.Context

import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie
import com.asmat.rolando.popularmovies.database.migrations.Migration_1_2
import com.asmat.rolando.popularmovies.database.migrations.Migration_2_3

/**
 * Database CRUD operations
 */
class DatabaseManager(context: Context) {

    companion object {
        private const val DATABASE_NAME = "movies-database"
    }

    private val dao: MoviesDAO // TODO convert into constructor dependency

    init {
        dao = Room.databaseBuilder(context,
                AppDatabase::class.java, DATABASE_NAME)
                .addMigrations(
                        Migration_1_2(),
                        Migration_2_3())
                .fallbackToDestructiveMigration()
                .build()
                .moviesDAO()
    }

    /**
     * Favorite movies
     */

    fun addFavoriteMovie(favoriteMovie: FavoriteMovie) {
        dao.insertFavoriteMovie(favoriteMovie)
    }

    fun getFavoriteMovie(id: Int): LiveData<FavoriteMovie> {
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