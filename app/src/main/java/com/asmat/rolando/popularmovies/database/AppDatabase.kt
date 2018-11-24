package com.asmat.rolando.popularmovies.database

import android.arch.persistence.room.*

import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie

@Database(entities = arrayOf(FavoriteMovie::class, WatchLaterMovie::class), version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moviesDAO(): MoviesDAO
}