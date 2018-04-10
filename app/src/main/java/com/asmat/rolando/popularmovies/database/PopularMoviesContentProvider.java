package com.asmat.rolando.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by rolandoasmat on 5/21/17.
 */

public class PopularMoviesContentProvider extends ContentProvider {

    private PopularMoviesDBHelper mDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int CODE_FAVORITES = 100;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopularMoviesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, PopularMoviesContract.PATH_FAVORITES, CODE_FAVORITES);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new PopularMoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITES:
                cursor = mDbHelper.getReadableDatabase().query(
                        PopularMoviesContract.FavoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri insertedUri;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITES:
                long id = mDbHelper.getWritableDatabase().insert(
                        PopularMoviesContract.FavoritesEntry.TABLE_NAME, null, values);
                insertedUri = ContentUris.withAppendedId(uri,id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return insertedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numOfRowsDeleted;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITES:
                numOfRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        PopularMoviesContract.FavoritesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return numOfRowsDeleted;
    }


    // Not implemented
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
