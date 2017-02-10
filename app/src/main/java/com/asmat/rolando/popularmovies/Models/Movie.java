package com.asmat.rolando.popularmovies.models;

import java.util.Date;

/**
 * Created by rolandoasmat on 2/9/17.
 */

public class Movie {

    String title;
    String posterURL;
    String plotSynopsis;
    double userRating;
    Date releaseDate;

    public Movie(String title, String posterURL, String plotSynopsis, double userRating, Date releaseDate) {
        this.title = title;
        this.posterURL = posterURL;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

}
