package com.asmat.rolando.popularmovies.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.database.FavoriteMovie
import com.asmat.rolando.popularmovies.database.Movie
import com.asmat.rolando.popularmovies.database.WatchLaterMovie
import com.asmat.rolando.popularmovies.managers.MoviesRepository
import com.asmat.rolando.popularmovies.models.Credit
import com.asmat.rolando.popularmovies.models.Review
import com.asmat.rolando.popularmovies.networking.models.VideosResponse

import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class MovieDetailsViewModel : ViewModel() {
    private var movieID: Int? = null
    var movie: Single<Movie>? = null
    var movieSubject: BehaviorSubject<Movie>? = BehaviorSubject.create()
    var favoriteMovie: LiveData<FavoriteMovie>? = null
    var watchLaterMovie: LiveData<WatchLaterMovie>? = null
    var videos: Single<VideosResponse>? = null
    var reviews: Single<List<Review>>? = null
    var credit: Single<Credit>? = null

    fun init(movieID: Int) {
        this.movieID = movieID
        initLiveData()
    }

    // Setup LiveData
    private fun initLiveData() {
        movieID?.let {
            movie = MoviesRepository.getMovie(it).map { Movie(it) }
            movie?.doOnSuccess { movieSubject?.onNext(it) }
            favoriteMovie = MoviesRepository.getFavoriteMovie(it)
            watchLaterMovie = MoviesRepository.getWatchLaterMovie(it)
            videos = MoviesRepository.getVideos(it)
            reviews = MoviesRepository.getReviews(it)
            credit = MoviesRepository.getMovieCreadits(it)
        }
    }
}