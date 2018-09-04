package com.asmat.rolando.popularmovies.adapters;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.models.MovieAdapterOnClickHandler;

public class WatchLaterMoviesGridAdapter extends MoviesGridBaseAdapter {
    public WatchLaterMoviesGridAdapter(MovieAdapterOnClickHandler handler) {
        super(handler);
    }

    @Override
    public int getEmptyStateLayoutID() {
        return R.layout.empty_state_watch_later;
    }
}