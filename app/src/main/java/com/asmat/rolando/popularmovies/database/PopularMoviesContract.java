package com.asmat.rolando.popularmovies.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by rolandoasmat on 5/21/17.
 */

final public class PopularMoviesContract {
    private PopularMoviesContract() {
        throw new AssertionError("No instances!");
    }

    public static final String CONTENT_AUTHORITY = "com.asmat.rolando.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    /* Inner class that defines the table contents */
    public static final class FavoritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_URL = "poster_url";
        public static final String COLUMN_BACKDROP_URL = "backdrop_url";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";

    }
}
