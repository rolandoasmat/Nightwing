package com.asmat.rolando.nightwing.database

import androidx.room.*

import com.asmat.rolando.nightwing.database.entities.SavedMovie

@Database(entities = [SavedMovie::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moviesDAO(): MoviesDAO
}