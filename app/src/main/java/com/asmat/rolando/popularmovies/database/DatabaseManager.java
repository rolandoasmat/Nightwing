package com.asmat.rolando.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
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
                AppDatabase.class, DATABASE_NAME)
                .addMigrations(MIGRATION_1_2)
                .build();
        this.db = db;
    }

    /**
     * Favorite movies
     */

    public LiveData<FavoriteMovie> getFavoriteMovie(int id) {
        MoviesDAO dao = db.moviesDAO();
        LiveData<FavoriteMovie> favoriteMovie = dao.findFavoriteMovie(id);
        return favoriteMovie;
    }

    public void deleteFavoriteMovie(final FavoriteMovie movie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MoviesDAO dao = db.moviesDAO();
                dao.deleteFavoriteMovie(movie);
                return null;
            }
        }.execute();
    }

    public void addFavoriteMovie(final FavoriteMovie favoriteMovie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MoviesDAO dao = db.moviesDAO();
                dao.insertFavoriteMovie(favoriteMovie);
                return null;
            }
        }.execute();
    }

    public LiveData<Movie[]> getFavoriteMovies() {
        MoviesDAO dao = db.moviesDAO();
        return dao.loadAllFavoriteMovies();
    }

    /**
     * Watch Later movies
     */

    public LiveData<WatchLaterMovie> getWatchLaterMovie(int id) {
        MoviesDAO dao = db.moviesDAO();
        LiveData<WatchLaterMovie> watchLaterMovie = dao.findWatchLaterMovie(id);
        return watchLaterMovie;
    }

    public void deleteWatchLaterMovie(final WatchLaterMovie movie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MoviesDAO dao = db.moviesDAO();
                dao.deleteWatchLaterMovie(movie);
                return null;
            }
        }.execute();
    }

    public void addWatchLaterMovie(final WatchLaterMovie watchLaterMovie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MoviesDAO dao = db.moviesDAO();
                dao.insertWatchLaterMovie(watchLaterMovie);
                return null;
            }
        }.execute();
    }

    public LiveData<Movie[]> getWatchLaterMovies() {
        MoviesDAO dao = db.moviesDAO();
        return dao.loadAllWatchLaterMovies();
    }

    /**
     * Movie
     */
    public void insertMovie(final Movie movie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MoviesDAO dao = db.moviesDAO();
                dao.insertMovie(movie);
                return null;
            }
        }.execute();
    }

    public void deleteMovie(final Movie movie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MoviesDAO dao = db.moviesDAO();
                dao.deleteMovie(movie);
                return null;
            }
        }.execute();
    }

    /**
     * Migrations
     */

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE `watch_later_movies` (`id` INTEGER NOT NULL, "
                    + " PRIMARY KEY(`id`),"
                    + " FOREIGN KEY(`id`) REFERENCES `movies`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)");
        }
    };
}