package com.asmat.rolando.popularmovies.managers;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.asmat.rolando.popularmovies.database.DatabaseManager;
import com.asmat.rolando.popularmovies.database.FavoriteMovie;
import com.asmat.rolando.popularmovies.database.Movie;
import com.asmat.rolando.popularmovies.database.WatchLaterMovie;
import com.asmat.rolando.popularmovies.models.Review;
import com.asmat.rolando.popularmovies.models.Video;

public class MoviesRepository {

    public static LiveData<Movie> getMovie(int movieID) {
        final MutableLiveData<Movie> data = new MutableLiveData<>();
        new AsyncTask<Integer, Void, Movie>() {
            @Override
            protected Movie doInBackground(Integer... integers) {
                try {
                    Movie movie = MovieApiManager.fetchMovie(integers[0]);
                    return movie;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Movie movie) {
                data.postValue(movie);
            }
        }.execute(movieID);
        return data;
    }

    public static LiveData<FavoriteMovie> getFavoriteMovie(int movieID) {
        return DatabaseManager.INSTANCE.getFavoriteMovie(movieID);
    }

    public static LiveData<WatchLaterMovie> getWatchLaterMovie(int movieID) {
        return DatabaseManager.INSTANCE.getWatchLaterMovie(movieID);
    }

    public static LiveData<Video[]> getVideos(int movieID) {
        final MutableLiveData<Video[]> data = new MutableLiveData<>();
        new AsyncTask<Integer, Void, Video[]>() {
            @Override
            protected Video[] doInBackground(Integer... integers) {
                try {
                    Video[] videos = MovieApiManager.fetchMovieVideos(integers[0]);
                    return videos;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Video[] videos) {
                data.postValue(videos);
            }
        }.execute(movieID);
        return data;
    }

    public static LiveData<Review[]> getReviews(int movieID) {
        final MutableLiveData<Review[]> data = new MutableLiveData<>();
        new AsyncTask<Integer, Void, Review[]>() {
            @Override
            protected Review[] doInBackground(Integer... integers) {
                try {
                    Review[] reviews = MovieApiManager.fetchMovieReviews(integers[0]);
                    return reviews;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Review[] reviews) {
                data.postValue(reviews);
            }
        }.execute(movieID);
        return data;
    }
}