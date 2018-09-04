package com.asmat.rolando.popularmovies.adapters;

import com.asmat.rolando.popularmovies.R;
import com.asmat.rolando.popularmovies.models.MovieAdapterOnClickHandler;

public class FavoriteMoviesGridAdapter extends MoviesGridBaseAdapter {
    public FavoriteMoviesGridAdapter(MovieAdapterOnClickHandler handler) {
        super(handler);
    }

    @Override
    public int getEmptyStateLayoutID() {
        return R.layout.empty_state_favorites;
    }
}
