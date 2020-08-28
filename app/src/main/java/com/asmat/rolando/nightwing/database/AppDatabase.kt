package com.asmat.rolando.nightwing.database

import androidx.room.*

import com.asmat.rolando.nightwing.database.entities.FavoriteMovie
import com.asmat.rolando.nightwing.database.entities.WatchLaterMovie

@Database(entities = [FavoriteMovie::class, WatchLaterMovie::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moviesDAO(): MoviesDAO
}