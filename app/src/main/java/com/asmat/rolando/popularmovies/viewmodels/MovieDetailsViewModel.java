package com.asmat.rolando.popularmovies.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.asmat.rolando.popularmovies.database.FavoriteMovie;
import com.asmat.rolando.popularmovies.database.Movie;
import com.asmat.rolando.popularmovies.models.Review;
import com.asmat.rolando.popularmovies.models.Video;

public class MovieDetailsViewModel extends ViewModel {
    private String movieID;
    private LiveData<Movie> movie;
    private LiveData<FavoriteMovie> favoriteMovie;
    private LiveData<Video[]> videos;
    private LiveData<Review[]> reviews;

    public void init(String movieID) {
        this.movieID = movieID;
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }

    public LiveData<FavoriteMovie> getFavoriteMovie() {
        return favoriteMovie;
    }

    public LiveData<Video[]> getVideos() {
        return videos;
    }

    public LiveData<Review[]> getReviews() {
        return reviews;
    }
}
