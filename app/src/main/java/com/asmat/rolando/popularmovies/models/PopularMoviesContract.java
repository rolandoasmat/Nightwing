package com.asmat.rolando.popularmovies.models;

import android.provider.BaseColumns;

/**
 * Created by rolandoasmat on 5/21/17.
 */

public final class PopularMoviesContract {
    private PopularMoviesContract() {}

    /* Inner class that defines the table contents */
    public static class FavoritesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_NAME_MOVIE_ID = "movie_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
        public static final String COLUMN_NAME_SYNOPSIS = "synopsis";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";

    }
}
