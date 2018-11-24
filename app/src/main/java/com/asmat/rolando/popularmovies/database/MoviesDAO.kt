package com.asmat.rolando.popularmovies.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie

@Dao
interface MoviesDAO {

    // Favorite

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteMovie(movie: FavoriteMovie)

    @Query("SELECT * FROM favorite_movies WHERE id LIKE :id")
    fun findFavoriteMovie(id: Int): LiveData<FavoriteMovie>

    @Delete
    fun deleteFavoriteMovie(movie: FavoriteMovie)

    @Query("SELECT * FROM favorite_movies")
    fun loadAllFavoriteMovies(): LiveData<List<FavoriteMovie>>

    // Watch Later

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWatchLaterMovie(movie: WatchLaterMovie)

    @Query("SELECT * FROM watch_later_movies WHERE id LIKE :id")
    fun findWatchLaterMovie(id: Int): LiveData<WatchLaterMovie>

    @Delete
    fun deleteWatchLaterMovie(movie: WatchLaterMovie)

    @Query("SELECT * FROM watch_later_movies")
    fun loadAllWatchLaterMovies(): LiveData<List<WatchLaterMovie>>

}