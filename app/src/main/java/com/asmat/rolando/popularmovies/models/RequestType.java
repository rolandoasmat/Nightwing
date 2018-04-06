package com.asmat.rolando.popularmovies.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rolandoasmat on 2/11/17.
 */

public class RequestType {
    public static final int MOST_POPULAR = 0;
    public static final int TOP_RATED = 1;


    @IntDef({MOST_POPULAR, TOP_RATED})
    @Retention(RetentionPolicy.SOURCE)

    public @interface Def {}

}

