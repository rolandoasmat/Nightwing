package com.asmat.rolando.nightwing.database

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepository @Inject constructor(private val db: AppDatabase) {

    fun getMovies() = db.moviesDAO().getAllSavedMovies()

}