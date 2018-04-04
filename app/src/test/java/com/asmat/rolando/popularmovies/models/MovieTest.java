package com.asmat.rolando.popularmovies.models;

import org.junit.Test;
import static org.junit.Assert.*;

public class MovieTest {

    @Test
    public void getReleaseDateFormatted() {
        Movie movie = getMovieTestObject();
        String actual = movie.getReleaseDateFormatted();
        String expected = "March 28, 2018";
        assertEquals(expected, actual);
    }

    @Test
    public void getReleaseDateFormattedInvalidDate() {
        Movie movie = getMovieTestObject();
        movie.setReleaseDate("not parcelable date string");
        String actual = movie.getReleaseDateFormatted();
        String expected = "Unable to parse date.";
        assertEquals(expected, actual);
    }

    @Test
    public void getBackdropURL() {
        Movie movie = getMovieTestObject();
        String actual = movie.getBackdropURL();
        String expected = "http://image.tmdb.org/t/p/w780/q7fXcrDPJcf6t3rzutaNwTzuKP1.jpg";
        assertEquals(expected, actual);
    }

    @Test
    public void getPosterURL() {
        Movie movie = getMovieTestObject();
        String actual = movie.getPosterURL();
        String expected = "http://image.tmdb.org/t/p/w342/pU1ULUq8D3iRxl1fdX2lZIzdHuI.jpg";
        assertEquals(expected, actual);
    }

    // Helper method to generate a test object
    private Movie getMovieTestObject() {
        int id = 333339;
        String title = "Ready Player One";
        String posterPath = "/pU1ULUq8D3iRxl1fdX2lZIzdHuI.jpg";
        String backdropPath = "/q7fXcrDPJcf6t3rzutaNwTzuKP1.jpg";
        String overview = "When the creator of a popular video game system dies, a virtual contest is created to compete for his fortune.";
        double voteAverage = 8.1;
        String releaseDate = "2018-03-28";
        return new Movie(id, title, posterPath, backdropPath, overview, voteAverage, releaseDate);
    }
}