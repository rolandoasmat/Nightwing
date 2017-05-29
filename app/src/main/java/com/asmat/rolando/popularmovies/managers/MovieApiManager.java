package com.asmat.rolando.popularmovies.managers;

import com.asmat.rolando.popularmovies.BuildConfig;
import com.asmat.rolando.popularmovies.models.Movie;
import com.asmat.rolando.popularmovies.models.RequestType;
import com.asmat.rolando.popularmovies.models.Review;
import com.asmat.rolando.popularmovies.models.Video;
import com.asmat.rolando.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private static final String GET_POPULAR     = "popular"; // GET /movie/popular
    private static final String GET_TOP_RATED   = "top_rated"; // GET/movie/top_rated
    private static final String GET_VIDEOS      = "videos"; // GET /movie/{movie_id}/videos
    private static final String GET_REVIEWS     = "reviews"; // GET /movie/{movie_id}/reviews

    /**
     * Universal Query Parameters
     */
    private static final String API_KEY_PARAM  = "api_key";
    private static final String API_KEY_VALUE  = BuildConfig.API_KEY;
    private static final String LANGUAGE_PARAM = "language";
    private static final String LANGUAGE_VALUE = Locale.getDefault().getLanguage();
    private static final String PAGE_PARAM     = "page";

    /**
     *  ---------------------------- API ----------------------------
     */

    public static Movie[] fetchMoviesOfType(int type, int page) throws IOException, JSONException, ParseException {
        switch(type) {
            case RequestType.MOST_POPULAR:
                return fetchMostPopularMovies(page);
            case RequestType.TOP_RATED:
                return fetchTopRatedMovies(page);
            default:
                throw new InvalidParameterException();
        }
    }

    /**
     * Fetch all the popular movies from movie API.
     *
     * @param page Page to fetch.
     *
     * @return Array of movies.
     *
     * @throws IOException
     * @throws JSONException
     * @throws ParseException
     */
    public static Movie[] fetchMostPopularMovies(int page) throws IOException, JSONException, ParseException {
        return fetchMovies(BASE_URL, MOVIES, GET_POPULAR, page);
    }

    /**
     * Fetch the top rated movies from movie API.
     *
     * @param page Page to fetch.
     *
     * @return Array of movies.
     *
     * @throws IOException
     * @throws JSONException
     * @throws ParseException
     */
    public static Movie[] fetchTopRatedMovies(int page) throws IOException, JSONException, ParseException {
        return fetchMovies(BASE_URL, MOVIES, GET_TOP_RATED, page);
    }

    /**
     * Fetch reviews of specified movie id.
     *
     * @param id ID of movie.
     *
     * @param page Page to fetch.
     *
     * @return Array of reviews.
     *
     * @throws IOException
     * @throws JSONException
     * @throws ParseException
     */
    public static Review[] fetchMovieReviews(int id, int page) throws IOException, JSONException, ParseException {
        return fetchReviews(id, BASE_URL, MOVIES, GET_REVIEWS, page);
    }

    /**
     * Fetch videos(trailers and teasers) of specified movie id.
     *
     * @param id ID of movie.
     *
     * @param page Page to fetch.
     *
     * @return Array of videos.
     *
     * @throws IOException
     * @throws JSONException
     * @throws ParseException
     */
    public static Video[] fetchMovieVideos(int id, int page) throws IOException, JSONException, ParseException {
        return fetchVideos(id, BASE_URL, MOVIES, GET_VIDEOS, page);
    }

    /**
     * ---------------------------- Private Methods ----------------------------
     */

    private static Movie[] fetchMovies(String baseURL,
                                       String subComponent,
                                       String endpoint,
                                       int page) throws IOException, JSONException, ParseException {
        // Make request
        String jsonResponse =  httpRequest(baseURL, subComponent, endpoint, page);
        // Map to Movie objects
        Movie[] movies = mapMovies(jsonResponse);
        return movies;
    }

    private static Review[] fetchReviews(int id,
                                         String baseURL,
                                         String subComponent,
                                         String endpoint,
                                         int page) throws IOException, JSONException, ParseException {
        // Make request
        String jsonResponse =  httpRequest(baseURL, subComponent+"/"+id, endpoint, page);
        // Map to Movie objects
        Review[] reviews = mapReviews(jsonResponse);
        return reviews;
    }

    private static Video[] fetchVideos(int id,
                                        String baseURL,
                                        String subComponent,
                                        String endpoint,
                                        int page) throws IOException, JSONException, ParseException {
        // Make request
        String jsonResponse =  httpRequest(baseURL, subComponent+"/"+id, endpoint, page);
        // Map to Movie objects
        Video[] videos = mapVideos(jsonResponse);
        return videos;
    }

    private static String httpRequest(String baseURL,
                                      String subComponent,
                                      String endpoint,
                                      int page) throws IOException, JSONException, ParseException {
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
        return  NetworkUtils.httpRequest(url);
    }

    /**
     * ---------------------------- Mappings ----------------------------
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

    private static Video[] mapVideos(String jsonStr) throws JSONException, ParseException {
        JSONObject forecastJson = new JSONObject(jsonStr);
        JSONArray results = forecastJson.getJSONArray("results");
        Video[] videos = new Video[results.length()];
        for(int i = 0; i < results.length(); i++){
            JSONObject movieJson = results.getJSONObject(i);
            videos[i] = mapVideo(movieJson);
        }
        return videos;
    }

    private static Review[] mapReviews(String jsonStr) throws JSONException, ParseException {
        JSONObject forecastJson = new JSONObject(jsonStr);
        JSONArray results = forecastJson.getJSONArray("results");
        Review[] reviews = new Review[results.length()];
        for(int i = 0; i < results.length(); i++){
            JSONObject movieJson = results.getJSONObject(i);
            reviews[i] = mapReview(movieJson);
        }
        return reviews;
    }

    private static Movie mapMovie(JSONObject json) throws JSONException, ParseException {
        String posterURL    = json.getString("poster_path");
        String plotSynopsis = json.getString("overview");
        String releaseDate  = json.getString("release_date");
        int id              = json.getInt("id");
        String title        = json.getString("original_title");
        String backdropURL  = json.getString("backdrop_path");
        double userRating   = json.getDouble("vote_average");
        return new Movie(id, title, posterURL, backdropURL, plotSynopsis, userRating, releaseDate);
    }

    private static Video mapVideo(JSONObject json) throws JSONException, ParseException {
        // Get properties
        String name = json.getString("name");
        String site = json.getString("site");
        String key  = json.getString("key");
        String type = json.getString("type");
        // Create Movie object
        return new Video(name, site, key, type);
    }

    private static Review mapReview(JSONObject json) throws JSONException, ParseException {
        // Get properties
        String author  = json.getString("author");
        String content = json.getString("content");
        // Create Movie object
        return new Review(author, content);
    }

}