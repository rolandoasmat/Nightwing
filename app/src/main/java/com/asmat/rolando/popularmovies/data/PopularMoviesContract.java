package com.asmat.rolando.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by rolandoasmat on 5/21/17.
 */

public class PopularMoviesContract {
    private PopularMoviesContract() {}

    public static final String CONTENT_AUTHORITY = "com.asmat.rolando.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    /* Inner class that defines the table contents */
    public static final class FavoritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_NAME_MOVIE_ID = "movie_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SYNOPSIS = "synopsis";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";

    }
}
