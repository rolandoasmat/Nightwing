package com.asmat.rolando.popularmovies.viewmodels

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.model.mappers.FavoriteMovieMapper
import com.asmat.rolando.popularmovies.model.mappers.WatchLaterMovieMapper
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.*
import com.asmat.rolando.popularmovies.utilities.DateUtils
import com.asmat.rolando.popularmovies.utilities.URLUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class MovieDetailsViewModel(private val moviesRepository: MoviesRepository,
                            private val movie: Movie) : ViewModel() {

    val backdropURL = MutableLiveData<String>()
    val movieTitle = MutableLiveData<String>()
    val releaseDate = MutableLiveData<String>()
    val rating = MutableLiveData<String>()
    val posterURL = MutableLiveData<String>()
    val summary = MutableLiveData<String>()

    val isFavoriteMovie: LiveData<Boolean>
    val isWatchLaterMovie: LiveData<Boolean>
    val shareMovie: MutableLiveData<Pair<String, String>>

    val videos: LiveData<List<VideosResponse.Video>>
    val cast: LiveData<List<CreditsResponse.Cast>>
    val reviews: LiveData<List<ReviewsResponse.Review>>
    
    private val movieID = movie.id

    init {
        movie.backdropPath?.let { backdropURL.value = URLUtils.getImageURL780(it) }
        movieTitle.value = movie.title
        movie.releaseDate.let { releaseDate.value = DateUtils.formatDate(it) }
        rating.value = movie.voteAverage.toString()
        movie.posterPath?.let { posterURL.value = URLUtils.getImageURL342(it) }
        summary.value = movie.overview

        isFavoriteMovie = Transformations.map(moviesRepository.getFavoriteMovie(movieID)) {
            it != null
        }

        isWatchLaterMovie = Transformations.map(moviesRepository.getWatchLaterMovie(movieID)) {
            it != null
        }

        shareMovie = MutableLiveData()

        videos = MutableLiveData<List<VideosResponse.Video>>()
        cast = MutableLiveData<List<CreditsResponse.Cast>>()
        reviews = MutableLiveData<List<ReviewsResponse.Review>>()

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

    fun onStarTapped() {
        isFavoriteMovie.value?.let {
            if (it) {
                moviesRepository.removeFavoriteMovie(movieID)
            } else {
                val favoriteMovie = FavoriteMovieMapper.from(movie)
                moviesRepository.insertFavoriteMovie(favoriteMovie)
            }
        }
    }

    fun onBookmarkTapped() {
        isWatchLaterMovie.value?.let {
            if (it) {
                moviesRepository.removeWatchLaterMovie(movieID)
            } else {
                val watchLaterMovie = WatchLaterMovieMapper.from(movie)
                moviesRepository.insertWatchLaterMovie(watchLaterMovie)
            }
        }
    }

    fun onShareTapped() {
        val movieTitle = movie.title
        val youtubeURL = videos.value?.firstOrNull()?.key?.let { URLUtils.getYoutubeURL(it) } ?: ""
        shareMovie.value = Pair(movieTitle, youtubeURL)
    }

}