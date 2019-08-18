package com.asmat.rolando.popularmovies.ui.moviedetails

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.*
import com.asmat.rolando.popularmovies.utilities.DateUtils
import com.asmat.rolando.popularmovies.utilities.URLUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class MovieDetailsViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {

    val backdropURL = MutableLiveData<String>()
    val movieTitle = MutableLiveData<String>()
    val releaseDate = MutableLiveData<String>()
    val rating = MutableLiveData<String>()
    val posterURL = MutableLiveData<String>()
    val summary = MutableLiveData<String>()

    lateinit var isFavoriteMovie: LiveData<Boolean>
    lateinit var isWatchLaterMovie: LiveData<Boolean>
    val shareMovie = MutableLiveData<Pair<String, String>>()

    val videos = MutableLiveData<List<VideosResponse.Video>>()
    val cast = MutableLiveData<List<CreditsResponse.Cast>>()
    val reviews = MutableLiveData<List<ReviewsResponse.Review>>()

    fun init(movie: MovieDetailsUIModel) {
        backdropURL.value = movie.backdropPath
        movieTitle.value = movie.title
        movie.releaseDate.let { releaseDate.value = DateUtils.formatDate(it) }
        rating.value = movie.voteAverage.toString()
        posterURL.value = movie.posterPath
        summary.value = movie.overview

        isFavoriteMovie = Transformations.map(moviesRepository.getFavoriteMovie(movie.id)) {
            it != null
        }

        isWatchLaterMovie = Transformations.map(moviesRepository.getWatchLaterMovie(movie.id)) {
            it != null
        }

        moviesRepository
                .getMovieVideos(movie.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    videos.value = result.results
                }, { _ ->
                    videos.value = null
                })

        moviesRepository
                .getMovieCredits(movie.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    cast.value = result.cast
                }, { _ ->
                    cast.value = null
                })

        moviesRepository
                .getMovieReviews(movie.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    reviews.value = result.results
                }, { _ ->
                    reviews.value = null
                })
    }

    //region UI events

    fun onStarTapped(movieID: Int) {
        isFavoriteMovie.value?.let {
            if (it) {
                moviesRepository.removeFavoriteMovie(movieID)
            } else {
//                val favoriteMovie = FavoriteMovieMapper.from(movie)
//                moviesRepository.insertFavoriteMovie(favoriteMovie)
            }
        }
    }

    fun onBookmarkTapped(movieID: Int) {
        isWatchLaterMovie.value?.let {
            if (it) {
                moviesRepository.removeWatchLaterMovie(movieID)
            } else {
//                val watchLaterMovie = WatchLaterMovieMapper.from(movie)
//                moviesRepository.insertWatchLaterMovie(watchLaterMovie)
            }
        }
    }

    fun onShareTapped() {
        val movieTitle = movieTitle.value ?: ""
        val youtubeURL = videos.value?.firstOrNull()?.key?.let { URLUtils.getYoutubeURL(it) } ?: ""
        shareMovie.value = Pair(movieTitle, youtubeURL)
    }

}