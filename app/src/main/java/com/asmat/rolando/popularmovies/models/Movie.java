package com.asmat.rolando.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by rolandoasmat on 2/9/17.
 */

public class Movie implements Parcelable {

    String title;
    String posterURL;
    String backdropURL;
    String plotSynopsis;
    double userRating;
    Date releaseDate;

    public Movie(String title, String posterURL, String backdropURL, String plotSynopsis, double userRating, Date releaseDate) {
        this.title = title;
        this.posterURL = posterURL;
        this.backdropURL = backdropURL;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.posterURL);
        dest.writeString(this.backdropURL);
        dest.writeString(this.plotSynopsis);
        dest.writeDouble(this.userRating);
        dest.writeLong(releaseDate.getTime());
    }

    protected Movie(Parcel in) {
        title = in.readString();
        posterURL = in.readString();
        backdropURL = in.readString();
        plotSynopsis = in.readString();
        userRating = in.readDouble();
        long tmpReleaseDate = in.readLong();
        releaseDate =  new Date(tmpReleaseDate);
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
