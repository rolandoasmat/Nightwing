package com.asmat.rolando.popularmovies.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MovieGridItemType {

    public static final int REGULAR = 0;
    public static final int EMPTY = 1;


    @IntDef({REGULAR, EMPTY})
    @Retention(RetentionPolicy.SOURCE)

    public @interface Def {}
}
