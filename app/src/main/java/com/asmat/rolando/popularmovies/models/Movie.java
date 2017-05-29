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
    private String posterURL;
    private String backdropURL;
    private String plotSynopsis;
    private double userRating;
    private String releaseDate;

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w342"; // "w92", "w154", "w185", "w342", "w500", "w780"
    final private String DATE_FORMAT = "MMMM dd, yyyy";

    public Movie(int id, String title, String posterURL, String backdropURL,
                 String plotSynopsis, double userRating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterURL = posterURL;
        this.backdropURL = backdropURL;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public String getReleaseDateFormatted() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(releaseDate);
            sdf = new SimpleDateFormat(DATE_FORMAT);
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Unable to parse date.";
        }
    }

    public String getBackdropUrlComplete() {
        return IMAGE_BASE_URL + backdropURL;
    }

    public String getPosterUrlComplete() {
        return IMAGE_BASE_URL + posterURL;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public String getbackdropURL() {
        return backdropURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public void setbackdropURL(String backdropURL) {
        this.backdropURL = backdropURL;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
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
        dest.writeString(posterURL);
        dest.writeString(backdropURL);
        dest.writeString(plotSynopsis);
        dest.writeDouble(userRating);
        dest.writeString(releaseDate);
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        posterURL = in.readString();
        backdropURL = in.readString();
        plotSynopsis = in.readString();
        userRating = in.readDouble();
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
