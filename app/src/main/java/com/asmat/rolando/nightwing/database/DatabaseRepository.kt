package com.asmat.rolando.nightwing.database

import com.asmat.rolando.nightwing.database.entities.SavedMovie
import com.asmat.rolando.nightwing.database.entities.SavedTvShow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepository @Inject constructor(private val db: AppDatabase) {

    fun getSavedMovie(id: Int) = db.moviesDAO().getSavedMovie(id)

    fun getSavedMovies() = db.moviesDAO().getAllSavedMovies()

    fun getSavedTvShow(id: Int) = db.tvShowsDAO().getSavedMovie(id)

    suspend fun insertSavedMovie(movie: SavedMovie) = db.moviesDAO().insertSavedMovie(movie)

    suspend fun deleteSavedMovie(id: Int) = db.moviesDAO().deleteSavedMovie(id)

    suspend fun deleteSavedTvShow(id: Int) = db.tvShowsDAO().deleteSavedTvShow(id)

    suspend fun insertSavedTvShow(data: SavedTvShow) = db.tvShowsDAO().insertSavedTvShow(data)
}