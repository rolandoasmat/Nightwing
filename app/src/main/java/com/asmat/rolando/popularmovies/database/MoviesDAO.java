package com.asmat.rolando.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;

@Dao
public interface MoviesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(FavoriteMovie movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM movies NATURAL JOIN favorite_movies")
    LiveData<Movie[]> loadAllFavoriteMovies();

    @Query("SELECT * FROM favorite_movies WHERE id LIKE :id")
    LiveData<FavoriteMovie> findFavoriteMovie(int id);
}
// todo refactor API