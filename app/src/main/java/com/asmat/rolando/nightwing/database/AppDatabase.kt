package com.asmat.rolando.nightwing.database

import androidx.room.*
import com.asmat.rolando.nightwing.database.entities.PopularMovie

import com.asmat.rolando.nightwing.database.entities.SavedMovie
import com.asmat.rolando.nightwing.database.entities.SavedTvShow

@Database(
    entities = [
        SavedMovie::class,
        SavedTvShow::class,
        PopularMovie::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moviesDAO(): MoviesDAO
    abstract fun tvShowsDAO(): TvShowsDAO
}