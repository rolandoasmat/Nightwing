package com.asmat.rolando.popularmovies.managers;

/**
 * Created by rolandoasmat on 2/10/17.
 */


import com.asmat.rolando.popularmovies.BuildConfig;
import com.asmat.rolando.popularmovies.models.Movie;
import com.asmat.rolando.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Locale;

/**
 * Utils for interacting with The Movie Database API
 * https://developers.themoviedb.org/3
 */
public final class MovieApiManager {

    /**
     * Base API url
     */
    private static final String BASE_URL = "https://api.themoviedb.org/3";

    /**
     * API sub-component
     */
    private static final String MOVIES = "movie";

    /**
     * Endpoints
     */
    private static final String GET_POPULAR   = "popular";
    private static final String GET_TOP_RATED = "top_rated";

    /**
     * Universal Query Parameters
     */
    private static final String API_KEY_PARAM  = "api_key";
    private static final String API_KEY_VALUE  = BuildConfig.API_KEY;
    private static final String LANGUAGE_PARAM = "language";
    private static final String LANGUAGE_VALUE = Locale.getDefault().getLanguage();
    private static final String PAGE_PARAM     = "page";

    /**
     *  Public METHODS ----------------------------------------------------------
     */

    public static Movie[] fetchPopularMovies(int page) throws IOException {
        return httpRequest(BASE_URL, MOVIES, GET_POPULAR, page);
    }

    public static Movie[] fetchTopRatedMovies(int page) throws IOException {
        return httpRequest(BASE_URL, MOVIES, GET_TOP_RATED, page);
    }

    /**
     *  Private METHODS ----------------------------------------------------------
     */

    private static Movie[] httpRequest(String baseURL, String subComponent, String endpoint, int page) throws IOException {
        // Concat url parts
        String completeUrl = baseURL+"/"+subComponent+"/"+endpoint;
        // Create params
        Hashtable<String, String> params = new Hashtable<>();
        params.put(API_KEY_PARAM, API_KEY_VALUE);
        params.put(LANGUAGE_PARAM, LANGUAGE_VALUE);
        params.put(PAGE_PARAM, Integer.toString(page));
        // Transform into URL
        URL url = NetworkUtils.buildUrl(completeUrl, params);
        // Make request
        String jsonResponse =  NetworkUtils.httpRequest(url);
        // Map to Movie objects
        Movie[] movies = mapMovies(jsonResponse);
        return movies;
    }

    private static Movie[] mapMovies(String json) {
        return null;
    }

    /**
     * Mappings
     *
     * Movie Model          | JSON Field Names (https://developers.themoviedb.org/3/movies/get-popular-movies)
     * ---------------------|-------------------------
     * String title;        | original_title
     * String posterURL;    | poster_path, may be null
     * String plotSynopsis; | overview
     * double userRating;   | vote_average, range of 0-10
     * Date releaseDate;    | release_date, format: yyyy-mm-dd
     */

}
