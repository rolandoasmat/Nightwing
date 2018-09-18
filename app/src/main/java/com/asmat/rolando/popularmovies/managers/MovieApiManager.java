package com.asmat.rolando.popularmovies.managers;

import com.asmat.rolando.popularmovies.BuildConfig;
import com.asmat.rolando.popularmovies.database.Movie;
import com.asmat.rolando.popularmovies.models.Cast;
import com.asmat.rolando.popularmovies.models.Credit;
import com.asmat.rolando.popularmovies.models.Crew;
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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

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
    private static final String SEARCH = "search";

    /**
     * Endpoints
     */
    private static final String MOVIES_POPULAR = "popular";     // /movie/popular
    private static final String MOVIES_TOP_RATED = "top_rated"; // /movie/top_rated
    private static final String MOVIES_NOW_PLAYING = "now_playing"; // /movie/top_rated
    private static final String MOVIES_UPCOMING = "upcoming"; // /movie/upcoming
    private static final String MOVIES_MOVIE = "";              // /movie/{movie_id}
    private static final String MOVIES_VIDEOS = "videos";       // /movie/{movie_id}/videos
    private static final String MOVIES_REVIEWS = "reviews";     // /movie/{movie_id}/reviews
    private static final String MOVIES_CREDITS = "credits";     // /movie/{movie_id}/credits

    private static final String SEARCH_MOVIES     = "movie"; // /search/movie

    /**
     * Universal Query Parameters
     */
    private static final String API_KEY_PARAM  = "api_key";
    private static final String API_KEY_VALUE  = BuildConfig.API_KEY;
    private static final String LANGUAGE_PARAM = "language";
    private static final String LANGUAGE_VALUE = Locale.getDefault().getLanguage();
    private static final String PARAM_REGION = "region";
    private static final String PARAM_REGION_VALUE = Locale.getDefault().getCountry();
    private static final String PAGE_PARAM     = "page";

    /**
     * OTHER Query Parameters
     */
    private static final String QUERY_PARAM = "query";

    /**
     *  ---------------------------- API ----------------------------
     */

    public static Movie fetchMovie(int id) throws IOException, JSONException {
        return fetchMovie(id, BASE_URL, MOVIES, MOVIES_MOVIE);
    }

    public static ArrayList<Movie> fetchMoviesOfType(@RequestType.Def int type, int page) throws IOException, JSONException {
        switch(type) {
            case RequestType.MOST_POPULAR:
                return fetchMostPopularMovies(page);
            case RequestType.TOP_RATED:
                return fetchTopRatedMovies(page);
            case RequestType.NOW_PLAYING:
                return fetchNowPlayingMovies(page);
            case RequestType.UPCOMING:
                return fetchUpcomingMovies(page);
            default:
                throw new InvalidParameterException();
        }
    }

    public static ArrayList<Movie> fetchSearchMovies(String query, int page) throws IOException, JSONException {
        Hashtable<String, String> queryParams = new Hashtable<>();
        queryParams.put(QUERY_PARAM, query);
        queryParams.put(PAGE_PARAM, String.valueOf(page));
        return fetchMovies(BASE_URL, SEARCH, SEARCH_MOVIES, queryParams);
    }

    public static Video[] fetchMovieVideos(int id) throws IOException, JSONException {
        return fetchVideos(id, BASE_URL, MOVIES, MOVIES_VIDEOS);
    }

    public static Review[] fetchMovieReviews(int id) throws IOException, JSONException {
        return fetchReviews(id, BASE_URL, MOVIES, MOVIES_REVIEWS);
    }

    public static Credit fetchMovieCredits(int id) throws IOException, JSONException {
        return fetchCredits(id, BASE_URL, MOVIES, MOVIES_CREDITS);
    }

    //region Private

    private static ArrayList<Movie> fetchMostPopularMovies(int page) throws IOException, JSONException {
        return fetchMovies(BASE_URL, MOVIES, MOVIES_POPULAR, page);
    }

    private static ArrayList<Movie> fetchTopRatedMovies(int page) throws IOException, JSONException {
        return fetchMovies(BASE_URL, MOVIES, MOVIES_TOP_RATED, page);
    }

    private static ArrayList<Movie> fetchNowPlayingMovies(int page) throws IOException, JSONException {
        return fetchMovies(BASE_URL, MOVIES, MOVIES_NOW_PLAYING, page);
    }

    private static ArrayList<Movie> fetchUpcomingMovies(int page) throws IOException, JSONException {
        return fetchMovies(BASE_URL, MOVIES, MOVIES_UPCOMING, page);
    }

    private static ArrayList<Movie> fetchMovies(String baseURL,
                                       String subComponent,
                                       String endpoint,
                                       int page) throws IOException, JSONException {
        String jsonResponse =  httpRequest(baseURL, subComponent, endpoint, page);
        return mapMovies(jsonResponse);
    }

    private static Movie fetchMovie(int id,
                                    String baseURL,
                                    String subComponent,
                                    String endpoint) throws IOException, JSONException {
        String jsonResponse =  httpRequest(baseURL, subComponent+"/"+id, endpoint);
        return mapMovie(jsonResponse);
    }

    private static ArrayList<Movie> fetchMovies(String baseURL,
                                                String subComponent,
                                                String endpoint,
                                                Hashtable<String, String> queryParams) throws IOException, JSONException {
        String jsonResponse =  httpRequest(baseURL, subComponent, endpoint, queryParams);
        return mapMovies(jsonResponse);
    }

    private static Review[] fetchReviews(int id,
                                         String baseURL,
                                         String subComponent,
                                         String endpoint) throws IOException, JSONException {
        String jsonResponse =  httpRequest(baseURL, subComponent+"/"+id, endpoint);
        return mapReviews(jsonResponse);
    }

    private static Video[] fetchVideos(int id,
                                        String baseURL,
                                        String subComponent,
                                        String endpoint) throws IOException, JSONException {
        String jsonResponse =  httpRequest(baseURL, subComponent+"/"+id, endpoint);
        return mapVideos(jsonResponse);
    }

    private static Credit fetchCredits(int id,
                                       String baseURL,
                                       String subComponent,
                                       String endpoint) throws IOException, JSONException {
        String jsonResponse =  httpRequest(baseURL, subComponent+"/"+id, endpoint);
        return mapCredit(jsonResponse);
    }



    //endregion

    /**
     * ---------------------------- Mappings ----------------------------
     */

    //region Mappings

    private static Movie mapMovie(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        return mapMovie(json);
    }

    private static Movie mapMovie(JSONObject json) throws JSONException {
        String posterPath    = json.getString("poster_path");
        String overview      = json.getString("overview");
        String releaseDate   = json.getString("release_date");
        int id               = json.getInt("id");
        String title         = json.getString("title");
        String backdropPath  = json.getString("backdrop_path");
        double userRating    = json.getDouble("vote_average");
        return new Movie(id, title, posterPath, backdropPath, overview, userRating, releaseDate);
    }

    private static ArrayList<Movie> mapMovies(String jsonStr) throws JSONException {
        ArrayList<Movie> movies = new ArrayList<>();
        JSONObject forecastJson = new JSONObject(jsonStr);
        JSONArray results = forecastJson.getJSONArray("results");
        for(int i = 0; i < results.length(); i++){
            JSONObject movieJson = results.getJSONObject(i);
            movies.add(mapMovie(movieJson));
        }
        return movies;
    }

    private static Video mapVideo(JSONObject json) throws JSONException {
        // Get properties
        String name = json.getString("name");
        String site = json.getString("site");
        String key  = json.getString("key");
        String type = json.getString("type");
        // Create Movie object
        return new Video(name, site, key, type);
    }

    private static Video[] mapVideos(String jsonStr) throws JSONException {
        JSONObject forecastJson = new JSONObject(jsonStr);
        JSONArray results = forecastJson.getJSONArray("results");
        Video[] videos = new Video[results.length()];
        for(int i = 0; i < results.length(); i++){
            JSONObject movieJson = results.getJSONObject(i);
            videos[i] = mapVideo(movieJson);
        }
        return videos;
    }

    private static Review mapReview(JSONObject json) throws JSONException {
        // Get properties
        String author  = json.getString("author");
        String content = json.getString("content");
        // Create Movie object
        return new Review(author, content);
    }

    private static Review[] mapReviews(String jsonStr) throws JSONException {
        JSONObject forecastJson = new JSONObject(jsonStr);
        JSONArray results = forecastJson.getJSONArray("results");
        Review[] reviews = new Review[results.length()];
        for(int i = 0; i < results.length(); i++){
            JSONObject movieJson = results.getJSONObject(i);
            reviews[i] = mapReview(movieJson);
        }
        return reviews;
    }

    private static Cast[] mapCast(JSONArray jsonArray) throws JSONException {
        Cast[] cast = new Cast[jsonArray.length()];
        for (int i = 0; i < cast.length; i++) {
            cast[i] = new Cast(jsonArray.getJSONObject(i));
        }
        return cast;
    }

    private static Crew[] mapCrew(JSONArray jsonArray) throws JSONException {
        Crew[] crew = new Crew[jsonArray.length()];
        for (int i = 0; i < crew.length; i++) {
            crew[i] = new Crew(jsonArray.getJSONObject(i));
        }
        return crew;
    }

    private static Credit mapCredit(String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        int id = jsonObject.getInt("id");
        Cast[] cast = mapCast(jsonObject.getJSONArray("cast"));
        Crew[] crew = mapCrew(jsonObject.getJSONArray("crew"));
        return new Credit(id, cast, crew);
    }

    //endregion

    //region HTTP

    private static String httpRequest(String baseURL,
                                      String subComponent,
                                      String endpoint) throws IOException {
        Hashtable<String,String> params = new Hashtable<>();
        return  httpRequest(baseURL, subComponent, endpoint, params);
    }

    private static String httpRequest(String baseURL,
                                      String subComponent,
                                      String endpoint,
                                      int page) throws IOException {
        Hashtable<String,String> params = new Hashtable<>();
        params.put(PAGE_PARAM, Integer.toString(page));
        params.put(PARAM_REGION, PARAM_REGION_VALUE);
        return  httpRequest(baseURL, subComponent, endpoint, params);
    }

    private static String httpRequest(String baseURL,
                                      String subComponent,
                                      String endpoint,
                                      Hashtable<String,String> queryParams) throws IOException {
        // Concat url parts
        StringBuilder urlBuilder = new StringBuilder(baseURL);
        if (subComponent != null && !subComponent.equals("")) {
            urlBuilder.append("/");
            urlBuilder.append(subComponent);
        }
        if (endpoint != null && !endpoint.equals("")) {
            urlBuilder.append("/");
            urlBuilder.append(endpoint);
        }

        // Create params
        Hashtable<String, String> params = new Hashtable<>();
        params.put(API_KEY_PARAM, API_KEY_VALUE);
        params.put(LANGUAGE_PARAM, LANGUAGE_VALUE);
        // Add queryParams
        params.putAll(queryParams);
        // Transform String into URL
        URL url = NetworkUtils.buildUrl(urlBuilder.toString(), params);
        // Make request
        return  NetworkUtils.httpRequest(url);
    }

    //endregion
}