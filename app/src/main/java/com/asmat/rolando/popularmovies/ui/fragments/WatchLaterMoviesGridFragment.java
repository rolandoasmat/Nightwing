package com.asmat.rolando.popularmovies.ui.fragments;

import android.arch.lifecycle.LiveData;
import com.asmat.rolando.popularmovies.ui.adapters.grid.MoviesGridBaseAdapter;
import com.asmat.rolando.popularmovies.ui.adapters.grid.WatchLaterMoviesGridAdapter;
import com.asmat.rolando.popularmovies.database.DatabaseManager;
import com.asmat.rolando.popularmovies.ui.adapters.MovieAdapterOnClickHandler;

public class WatchLaterMoviesGridFragment extends BaseGridFragment {

    @Override
    MoviesGridBaseAdapter getAdapter(MovieAdapterOnClickHandler handler) {
        return new WatchLaterMoviesGridAdapter(handler);
    }

    @Override
    LiveData<Movie[]> getMovieSource() {
        return DatabaseManager.INSTANCE.getWatchLaterMovies();
    }
}