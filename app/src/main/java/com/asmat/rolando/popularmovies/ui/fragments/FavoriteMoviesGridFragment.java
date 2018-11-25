package com.asmat.rolando.popularmovies.ui.fragments;

import android.arch.lifecycle.LiveData;

import com.asmat.rolando.popularmovies.ui.adapters.grid.FavoriteMoviesGridAdapter;
import com.asmat.rolando.popularmovies.ui.adapters.grid.MoviesGridBaseAdapter;
import com.asmat.rolando.popularmovies.database.DatabaseManager;
import com.asmat.rolando.popularmovies.ui.adapters.MovieAdapterOnClickHandler;

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