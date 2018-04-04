package com.asmat.rolando.popularmovies.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.asmat.rolando.popularmovies.data.PopularMoviesContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rolandoasmat on 2/9/17.
 */

public class Movie implements Parcelable {

    private int id;
    private String title;
    private String posterPath;
    private String backdropPath;
    private String overview;
    private double voteAverage;
    private String releaseDate;

    // "w92", "w154", "w185", "w342", "w500", "w780"
    private static final String IMAGE_BASE_URL_POSTER = "http://image.tmdb.org/t/p/w342";
    private static final String IMAGE_BASE_URL_BACKDROP = "http://image.tmdb.org/t/p/w780";

    private static final String DATE_FORMAT_ORIGINAL = "yyyy-MM-dd";
    private static final String DATE_FORMAT_DESIRED = "MMMM dd, yyyy";

    public Movie(int id, String title, String posterPath, String backdropPath,
                 String overview, double voteAverage, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public String getReleaseDateFormatted() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_ORIGINAL);
            Date date = sdf.parse(releaseDate);
            sdf = new SimpleDateFormat(DATE_FORMAT_DESIRED);
            String formatted = sdf.format(date);
            String uppercased = formatted.substring(0, 1).toUpperCase() + formatted.substring(1);
            return uppercased;
        } catch (ParseException e) {
            return "Unable to parse date.";
        }
    }

    public String getBackdropURL() {
        return IMAGE_BASE_URL_BACKDROP + this.backdropPath;
    }

    public String getPosterURL() {
        return IMAGE_BASE_URL_POSTER + this.posterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
    }

    /**
     * PARCELABLE
     */

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /**
     * Cursor
     */

    public static Movie getMovieFromCursorEntry(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COLUMN_MOVIE_ID));
        String title = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COLUMN_TITLE));
        String posterUrl = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COLUMN_POSTER_URL));
        String backdropUrl = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COLUMN_BACKDROP_URL));
        String synopsis = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COLUMN_SYNOPSIS));
        double rating = cursor.getDouble(cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COLUMN_RATING));
        String releaseDate = cursor.getString(cursor.getColumnIndex(PopularMoviesContract.FavoritesEntry.COLUMN_RELEASE_DATE));
        return new Movie(id, title, posterUrl, backdropUrl, synopsis, rating, releaseDate);
    }
}
