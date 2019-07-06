package com.asmat.rolando.popularmovies.ui.moviedetails

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
    
    private var movie: Movie? = null

    fun init(movie: Movie) {
        this.movie = movie
        movie.backdropPath?.let { backdropURL.value = URLUtils.getImageURL780(it) }
        movieTitle.value = movie.title
        movie.releaseDate.let { releaseDate.value = DateUtils.formatDate(it) }
        rating.value = movie.voteAverage.toString()
        movie.posterPath?.let { posterURL.value = URLUtils.getImageURL342(it) }
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

    fun onStarTapped() {
        val movie = this.movie ?: return
        isFavoriteMovie.value?.let {
            if (it) {
                moviesRepository.removeFavoriteMovie(movie.id)
            } else {
                val favoriteMovie = FavoriteMovieMapper.from(movie)
                moviesRepository.insertFavoriteMovie(favoriteMovie)
            }
        }
    }

    fun onBookmarkTapped() {
        val movie = this.movie ?: return
        isWatchLaterMovie.value?.let {
            if (it) {
                moviesRepository.removeWatchLaterMovie(movie.id)
            } else {
                val watchLaterMovie = WatchLaterMovieMapper.from(movie)
                moviesRepository.insertWatchLaterMovie(watchLaterMovie)
            }
        }
    }

    fun onShareTapped() {
        val movie = this.movie ?: return
        val movieTitle = movie.title
        val youtubeURL = videos.value?.firstOrNull()?.key?.let { URLUtils.getYoutubeURL(it) } ?: ""
        shareMovie.value = Pair(movieTitle, youtubeURL)
    }

}