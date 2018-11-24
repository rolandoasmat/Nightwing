package com.asmat.rolando.popularmovies.ui.adapters;

import com.asmat.rolando.popularmovies.R;

public class FavoriteMoviesGridAdapter extends MoviesGridBaseAdapter {
    public FavoriteMoviesGridAdapter(MovieAdapterOnClickHandler handler) {
        super(handler);
    }

    @Override
    public int getEmptyStateLayoutID() {
        return R.layout.empty_state_favorites;
    }
}
