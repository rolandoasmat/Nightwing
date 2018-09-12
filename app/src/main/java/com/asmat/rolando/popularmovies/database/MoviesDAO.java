package com.asmat.rolando.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;

@Dao
public interface MoviesDAO {

    // Movie

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    // Favorite

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(FavoriteMovie movie);

    @Delete
    void deleteFavoriteMovie(FavoriteMovie movie);

    @Query("SELECT * FROM favorite_movies WHERE id LIKE :id")
    LiveData<FavoriteMovie> findFavoriteMovie(int id);

    @Query("SELECT * FROM movies NATURAL JOIN favorite_movies")
    LiveData<Movie[]> loadAllFavoriteMovies();

    // Watch Later

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWatchLaterMovie(WatchLaterMovie movie);

    @Delete
    void deleteWatchLaterMovie(WatchLaterMovie movie);

    @Query("SELECT * FROM watch_later_movies WHERE id LIKE :id")
    LiveData<WatchLaterMovie> findWatchLaterMovie(int id);

    @Query("SELECT * FROM movies NATURAL JOIN watch_later_movies")
    LiveData<Movie[]> loadAllWatchLaterMovies();

}