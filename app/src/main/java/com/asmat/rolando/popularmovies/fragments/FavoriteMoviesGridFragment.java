package com.asmat.rolando.popularmovies.fragments;

import android.arch.lifecycle.LiveData;

import com.asmat.rolando.popularmovies.adapters.FavoriteMoviesGridAdapter;
import com.asmat.rolando.popularmovies.adapters.MoviesGridBaseAdapter;
import com.asmat.rolando.popularmovies.database.DatabaseManager;
import com.asmat.rolando.popularmovies.database.Movie;
import com.asmat.rolando.popularmovies.models.MovieAdapterOnClickHandler;

public class FavoriteMoviesGridFragment extends BaseGridFragment {

    public FavoriteMoviesGridFragment() { }

    @Override
    MoviesGridBaseAdapter getAdapter(MovieAdapterOnClickHandler handler) {
        return new FavoriteMoviesGridAdapter(handler);
    }

    @Override
    LiveData<Movie[]> getMovieSource() {
        return DatabaseManager.INSTANCE.getFavoriteMovies();
    }

}