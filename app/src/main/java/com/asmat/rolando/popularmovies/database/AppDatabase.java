package com.asmat.rolando.popularmovies.database;

import android.arch.persistence.room.*;

@Database(entities = {FavoriteMovie.class, Movie.class, WatchLaterMovie.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MoviesDAO moviesDAO();
}