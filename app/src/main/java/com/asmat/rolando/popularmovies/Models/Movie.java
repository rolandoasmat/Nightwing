package com.asmat.rolando.popularmovies.models;

import java.util.Date;

/**
 * Created by rolandoasmat on 2/9/17.
 */

public class Movie {

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

}
