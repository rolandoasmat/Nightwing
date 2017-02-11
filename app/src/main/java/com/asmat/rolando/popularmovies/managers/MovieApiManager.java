package com.asmat.rolando.popularmovies.managers;

import com.asmat.rolando.popularmovies.BuildConfig;
import com.asmat.rolando.popularmovies.models.Movie;
import com.asmat.rolando.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

/**
 * Created by rolandoasmat on 2/10/17.
 */

/**
 * Utils for communicating with The Movie Database API
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

    /**
     * Fetch all the popular movies from movie API.
     *
     * @param page Which page to get.
     *
     * @return Array of movie objects.
     *
     * @throws IOException
     * @throws JSONException
     */
    public static Movie[] fetchPopularMovies(int page) throws IOException, JSONException, ParseException {
        return httpRequest(BASE_URL, MOVIES, GET_POPULAR, page);
    }

    /**
     * Fetch the top rated movies from movie API.
     *
     * @param page Which page to get.
     *
     * @return Array of movie objects.
     *
     * @throws IOException
     * @throws JSONException
     */
    public static Movie[] fetchTopRatedMovies(int page) throws IOException, JSONException, ParseException {
        return httpRequest(BASE_URL, MOVIES, GET_TOP_RATED, page);
    }

    /**
     *  Private METHODS ----------------------------------------------------------
     */

    /**
     * Helper method to make http request.
     *
     * @param baseURL Base url of endpoint.
     * @param subComponent Sub component of endpoint.
     * @param endpoint Endpoint.
     * @param page Which page to return.
     *
     * @return Array of movie objects.
     *
     * @throws IOException
     * @throws JSONException
     */
    private static Movie[] httpRequest(String baseURL, String subComponent, String endpoint, int page) throws IOException, JSONException, ParseException {
        // Concat url parts
        String completeUrl = baseURL+"/"+subComponent+"/"+endpoint;
        // Create params
        Hashtable<String, String> params = new Hashtable<>();
        params.put(API_KEY_PARAM, API_KEY_VALUE);
        params.put(LANGUAGE_PARAM, LANGUAGE_VALUE);
        params.put(PAGE_PARAM, Integer.toString(page));
        // Transform String into URL
        URL url = NetworkUtils.buildUrl(completeUrl, params);
        // Make request
        String jsonResponse =  NetworkUtils.httpRequest(url);
        // Map to Movie objects
        Movie[] movies = mapMovies(jsonResponse);
        return movies;
    }

    /**
     * Mappings ----------------------------------------------------------------
     */

    /**
     * Map json response into array of Movie objects.
     *
     * @param jsonStr JSON response from API.
     *
     * @return Array of Movie objects.
     *
     * @throws JSONException
     */
    private static Movie[] mapMovies(String jsonStr) throws JSONException, ParseException {
        JSONObject forecastJson = new JSONObject(jsonStr);
        JSONArray results = forecastJson.getJSONArray("results");
        Movie[] movies = new Movie[results.length()];
        for(int i = 0; i < results.length(); i++){
            JSONObject movieJson = results.getJSONObject(i);
            movies[i] = mapMovie(movieJson);
        }
        return movies;
    }

    /**
     * Maps a Movie json object into Movie model
     *
     * Movie Model          | JSON Field Names (https://developers.themoviedb.org/3/movies/get-popular-movies)
     * ---------------------|-------------------------
     * String title;        | original_title
     * String posterURL;    | poster_path, may be null
     * String plotSynopsis; | overview
     * double userRating;   | vote_average, range of 0-10
     * Date releaseDate;    | release_date, format: yyyy-mm-dd
     *
     * @param json Movie json object
     *
     * @return Movie object
     */
    private static Movie mapMovie(JSONObject json) throws JSONException, ParseException {
        // Get properties
        String title           = json.getString("original_title");
        String posterURL       = "http://image.tmdb.org/t/p/w342/"+json.getString("poster_path");
        String plotSynopsis    = json.getString("overview");
        double userRating      = json.getDouble("vote_average");
        String releaseDateStr  = json.getString("release_date");
        // Create Date object
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDate = dateFormat.parse(releaseDateStr);
        // Create Movie object
        return new Movie(title,posterURL,plotSynopsis,userRating,releaseDate);
    }

}
