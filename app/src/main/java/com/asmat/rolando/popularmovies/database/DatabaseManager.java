package com.asmat.rolando.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

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

    public LiveData<FavoriteMovie> getFavoriteMovie(int id) {
        MoviesDAO dao = db.moviesDAO();
        LiveData<FavoriteMovie> favoriteMovie = dao.findFavoriteMovie(id);
        return favoriteMovie;
    }

    public void addFavoriteMovie(final Movie movie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MoviesDAO dao = db.moviesDAO();
                dao.insertMovie(movie);
                FavoriteMovie favoriteMovie = new FavoriteMovie();
                favoriteMovie.id = movie.getId();
                dao.insertFavoriteMovie(favoriteMovie);
                return null;
            }
        }.execute();
    }

    public void removeFavoriteMovie(final Movie movie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MoviesDAO dao = db.moviesDAO();
                dao.deleteMovie(movie);
                return null;
            }
        }.execute();
    }

    public LiveData<Movie[]> getFavoriteMovies() {
        MoviesDAO dao = db.moviesDAO();
        return dao.loadAllFavoriteMovies();
    }
}
