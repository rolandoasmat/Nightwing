package com.asmat.rolando.nightwing.database

import com.asmat.rolando.nightwing.database.entities.PopularMovie
import com.asmat.rolando.nightwing.database.entities.SavedMovie
import com.asmat.rolando.nightwing.database.entities.SavedTvShow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepository @Inject constructor(private val db: AppDatabase) {

    // Movies

    fun getSavedMovie(id: Int) = db.moviesDAO().getSavedMovie(id)

    fun getSavedMovies() = db.moviesDAO().getAllSavedMovies()

    //region Popular Movies

    suspend fun insertAllPopularMovies(movies: List<PopularMovie>) = db.moviesDAO().insertAllPopularMovies(movies)

    fun popularMoviesPagingSource() = db.moviesDAO().popularMoviesPagingSource()

    suspend fun clearAllPopularMovies() = db.moviesDAO().clearAllPopularMovies()

    //endregion

    suspend fun insertSavedMovie(movie: SavedMovie) = db.moviesDAO().insertSavedMovie(movie)

    suspend fun deleteSavedMovie(id: Int) = db.moviesDAO().deleteSavedMovie(id)


    // Tv Shows

    fun getSavedTvShow(id: Int) = db.tvShowsDAO().getSavedTvShow(id)

    fun getAllSavedTvShows() = db.tvShowsDAO().getAllSavedTvShows()

    suspend fun insertSavedTvShow(data: SavedTvShow) = db.tvShowsDAO().insertSavedTvShow(data)

    suspend fun deleteSavedTvShow(id: Int) = db.tvShowsDAO().deleteSavedTvShow(id)

}