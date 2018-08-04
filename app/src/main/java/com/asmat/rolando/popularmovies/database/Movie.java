package com.asmat.rolando.popularmovies.database;

import android.arch.persistence.room.*;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "movies", indices = {@Index("id")})
public class Movie implements Parcelable {

    @PrimaryKey
    private int id;
    private String title;

    @ColumnInfo(name = "poster_path")
    private String posterPath;

    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;

    private String overview;

    @ColumnInfo(name = "vote_average")
    private double voteAverage;

    @ColumnInfo(name = "release_date")
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

    /**
     * PARCELABLE
     */

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
}
