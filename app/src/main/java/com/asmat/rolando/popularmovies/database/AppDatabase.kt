package com.asmat.rolando.popularmovies.database

import androidx.room.*

import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie

@Database(entities = [FavoriteMovie::class, WatchLaterMovie::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moviesDAO(): MoviesDAO
}