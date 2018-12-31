package com.asmat.rolando.popularmovies.viewmodels

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie
import com.asmat.rolando.popularmovies.model.MoviesRepository
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.CreditsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MovieDetailsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.ReviewsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.VideosResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class MovieDetailsViewModel(moviesRepository: MoviesRepository,
                            movieID: Int) : ViewModel() {

    val movieDetails: LiveData<MovieDetailsResponse>
    val favoriteMovie: LiveData<FavoriteMovie>
    val watchLaterMovie: LiveData<WatchLaterMovie>
    val videos: LiveData<List<VideosResponse.Video>>
    val cast: LiveData<List<CreditsResponse.Cast>>
    val reviews: LiveData<List<ReviewsResponse.Review>>



    init {
        movieDetails = MutableLiveData<MovieDetailsResponse>()
        favoriteMovie = moviesRepository.getFavoriteMovie(movieID) // TODO figure out how to map this into a LiveData object
        watchLaterMovie = moviesRepository.getWatchLaterMovie(movieID)
        videos = MutableLiveData<List<VideosResponse.Video>>()
        cast = MutableLiveData<List<CreditsResponse.Cast>>()
        reviews = MutableLiveData<List<ReviewsResponse.Review>>()

        moviesRepository // TODO should something be done with these subscriptions?
                .getMovieDetails(movieID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    movieDetails.value = result
                }, { _ ->
                    movieDetails.value = null
                })

        moviesRepository
                .getMovieVideos(movieID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    videos.value = result.results
                }, { _ ->
                    videos.value = null
                })

        moviesRepository
                .getMovieCredits(movieID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    cast.value = result.cast
                }, { _ ->
                    cast.value = null
                })

        moviesRepository
                .getMovieReviews(movieID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    reviews.value = result.results
                }, { _ ->
                    reviews.value = null
                })
    }

    //region UI events

    fun onStarTapped() { // TODO implement UI events

    }

    fun onBookmarkTapped() {

    }

}