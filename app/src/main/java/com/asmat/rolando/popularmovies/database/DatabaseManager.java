package com.asmat.rolando.popularmovies.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public enum DatabaseManager {

    INSTANCE;

    private AppDatabase db;
    private final String DATABASE_NAME = "movies-database";

    public AppDatabase getInstance() {
        return this.db;
    }

    public void setInstance(Context applicationContext) {
        AppDatabase db = Room.databaseBuilder(applicationContext,
                AppDatabase.class, DATABASE_NAME).build();
        this.db = db;
    }

    public boolean isFavoriteMovie(int id) {
        MoviesDAO dao = db.moviesDAO();
        FavoriteMovie favoriteMovie = dao.findFavoriteMovie(id);
        return favoriteMovie != null;
    }

    public void addFavoriteMovie(Movie movie) {
        MoviesDAO dao = db.moviesDAO();
        dao.insertMovie(movie);
        FavoriteMovie favoriteMovie = new FavoriteMovie();
        favoriteMovie.id = movie.getId();
        dao.insertFavoriteMovie(favoriteMovie);
    }

    public void removeFavoriteMovie(Movie movie) {
        MoviesDAO dao = db.moviesDAO();
        dao.deleteMovie(movie);
    }

    public Movie[] getFavoriteMovies() {
        MoviesDAO dao = db.moviesDAO();
        return dao.loadAllFavoriteMovies();
    }
}
