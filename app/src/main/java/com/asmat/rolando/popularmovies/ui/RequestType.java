package com.asmat.rolando.popularmovies.ui;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RequestType {
    public static final int MOST_POPULAR = 0;
    public static final int TOP_RATED = 1;
    public static final int NOW_PLAYING = 2;
    public static final int UPCOMING = 3;

    @IntDef({MOST_POPULAR, TOP_RATED, NOW_PLAYING, UPCOMING})
    @Retention(RetentionPolicy.SOURCE)

    public @interface Def {}

}

