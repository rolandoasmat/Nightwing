package com.asmat.rolando.popularmovies.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.content.Context

import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie

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
                        migration_1_2(),
                        migration_2_3())
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

    fun deleteFavoriteMovie(movie: FavoriteMovie) {
        dao.deleteFavoriteMovie(movie)
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

    fun deleteWatchLaterMovie(movie: WatchLaterMovie) {
        dao.deleteWatchLaterMovie(movie)
    }

    fun getAllWatchLaterMovies(): LiveData<List<WatchLaterMovie>> {
        return dao.loadAllWatchLaterMovies()
    }

    /**
     * Migrations
     */

    fun migration_1_2(): Migration {
        return object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                        "CREATE TABLE `watch_later_movies` (`id` INTEGER NOT NULL, "
                                + " PRIMARY KEY(`id`),"
                                + " FOREIGN KEY(`id`) REFERENCES `movies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
            }
        }
    }

    fun migration_2_3(): Migration {
        return object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // TODO update watch later and favorite movies with new columns. delete movies table
                database.execSQL(
                        "CREATE TABLE `watch_later_movies` (`id` INTEGER NOT NULL, "
                                + " PRIMARY KEY(`id`),"
                                + " FOREIGN KEY(`id`) REFERENCES `movies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
            }
        }
    }

}