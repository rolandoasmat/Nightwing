package com.asmat.rolando.popularmovies.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

import com.asmat.rolando.popularmovies.database.FavoriteMovie
import com.asmat.rolando.popularmovies.database.Movie
import com.asmat.rolando.popularmovies.database.WatchLaterMovie
import com.asmat.rolando.popularmovies.managers.MoviesRepository
import com.asmat.rolando.popularmovies.models.Cast

import com.asmat.rolando.popularmovies.models.Review
import com.asmat.rolando.popularmovies.models.Video

import io.reactivex.Single

data class MovieDetailsViewModel(val movieID: Int) : ViewModel() {
    var movie: Single<Movie>? = null
    var favoriteMovie: LiveData<FavoriteMovie>? = null
    var watchLaterMovie: LiveData<WatchLaterMovie>? = null
    var videos: Single<List<Video>>? = null
    var reviews: Single<List<Review>>? = null
    var credit: Single<List<Cast>>? = null

    init {
        initLiveData()
    }

    // Setup LiveData
    private fun initLiveData() {
        movie = MoviesRepository.getMovie(movieID)
        favoriteMovie = MoviesRepository.getFavoriteMovie(movieID)
        watchLaterMovie = MoviesRepository.getWatchLaterMovie(movieID)
        videos = MoviesRepository.getVideos(movieID)
        reviews = MoviesRepository.getReviews(movieID)
        credit = MoviesRepository.getMovieCreadits(movieID)
    }

}