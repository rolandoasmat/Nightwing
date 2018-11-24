package com.asmat.rolando.popularmovies.ui.adapters;

import com.asmat.rolando.popularmovies.R;

public class WatchLaterMoviesGridAdapter extends MoviesGridBaseAdapter {
    public WatchLaterMoviesGridAdapter(MovieAdapterOnClickHandler handler) {
        super(handler);
    }

    @Override
    public int getEmptyStateLayoutID() {
        return R.layout.empty_state_watch_later;
    }
}