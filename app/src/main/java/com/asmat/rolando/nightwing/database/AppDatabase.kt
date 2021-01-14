package com.asmat.rolando.nightwing.database

import androidx.room.*

import com.asmat.rolando.nightwing.database.entities.SavedMovie
import com.asmat.rolando.nightwing.database.entities.SavedTvShow

@Database(entities = [SavedMovie::class, SavedTvShow::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moviesDAO(): MoviesDAO
    abstract fun tvShowsDAO(): TvShowsDAO
}