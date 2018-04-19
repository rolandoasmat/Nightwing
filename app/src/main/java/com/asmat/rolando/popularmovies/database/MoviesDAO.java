package com.asmat.rolando.popularmovies.database;

import android.arch.persistence.room.*;

@Dao
public interface MoviesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertFavoriteMovie(FavoriteMovie movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMovie(Movie movie);

    @Delete
    public void deleteMovie(Movie movie);

    @Query("SELECT * FROM movies NATURAL JOIN favorite_movies")
    public Movie[] loadAllFavoriteMovies();

    @Query("SELECT * FROM favorite_movies WHERE id LIKE :id")
    public FavoriteMovie findFavoriteMovie(int id);
}
