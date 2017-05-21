package com.asmat.rolando.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.asmat.rolando.popularmovies.data.PopularMoviesContract.*;
/**
 * Created by rolandoasmat on 5/21/17.
 */

public class PopularMoviesDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PopularMovies.db";

    public PopularMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + FavoritesEntry.TABLE_NAME + " (" +
                FavoritesEntry.COLUMN_NAME_MOVIE_ID + " INTEGER PRIMARY KEY," +
                FavoritesEntry.COLUMN_NAME_NAME + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_NAME_SYNOPSIS + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_NAME_RATING + " TEXT NOT NULL" +
                FavoritesEntry.COLUMN_NAME_RELEASE_DATE + " TEXT NOT NULL" +
                "); ";
        db.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME);
        onCreate(db);

    }
}
