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
    public void getBackdropUrlComplete() {
    }

    @Test
    public void getPosterUrlComplete() {
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
        Movie movie = new Movie(id, title, posterPath, backdropPath, overview, voteAverage, releaseDate);
        return movie;
    }
}