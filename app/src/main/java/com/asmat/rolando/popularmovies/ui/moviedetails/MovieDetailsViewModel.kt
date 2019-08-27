package com.asmat.rolando.popularmovies.ui.moviedetails

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.model.mappers.FavoriteMovieMapper
import com.asmat.rolando.popularmovies.model.mappers.WatchLaterMovieMapper
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.CreditsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.ReviewsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.VideosResponse
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.utilities.DateUtils
import com.asmat.rolando.popularmovies.utilities.URLUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Movie Details Screen view model
 */
@SuppressLint("CheckResult")
class MovieDetailsViewModel @Inject constructor(
        private val moviesRepository: MoviesRepository
) : ViewModel() {

    val backdropURL = MutableLiveData<String>()
    val movieTitle = MutableLiveData<String>()
    val releaseDate = MutableLiveData<String>()
    val rating = MutableLiveData<String>()
    val posterURL = MutableLiveData<String>()
    val summary = MutableLiveData<String>()

    val isFavoriteMovie = MutableLiveData<Boolean>()
    val isWatchLaterMovie = MutableLiveData<Boolean>()
    val shareMovie = MutableLiveData<Pair<String, String>>()

    val videos = MutableLiveData<List<VideosResponse.Video>>()
    val videosError = MutableLiveData<Throwable>()

    val cast = MutableLiveData<List<CreditsResponse.Cast>>()
    val castError = MutableLiveData<Throwable>()

    val reviews = MutableLiveData<List<ReviewsResponse.Review>>()
    val reviewsError = MutableLiveData<Throwable>()



    private var uiModel: MovieDetailsUIModel? = null
    set(value) {
        handleUiModel(value)
    }

    private val movieDetailsRawData: Movie? get() { return moviesRepository.getMovieDetailsData() }

    init {
        movieDetailsRawData?.let { movie ->
            uiModel = map(movie)
            fetchData(movie.id)
        }
    }

    //region API

    /**
     * User pressed the star, "favorite movie", icon
     */
    fun onStarTapped() {
        val movie = movieDetailsRawData ?: return
        isFavoriteMovie.value?.let {
            if (it) {
                // It's a favorite movie, "un-favorite" it
                moviesRepository.removeFavoriteMovie(movie.id)
            } else {
                // It's not a favorite movie, "favorite" it
                val favoriteMovie = FavoriteMovieMapper.from(movie)
                moviesRepository.insertFavoriteMovie(favoriteMovie)
            }
        }
    }

    /**
     * User pressed the bookmark, "watch later movie", icon
     */
    fun onBookmarkTapped() {
        val movie = movieDetailsRawData ?: return
        isWatchLaterMovie.value?.let {
            if (it) {
                // It's a watch later movie, "un-watch-later" it
                moviesRepository.removeWatchLaterMovie(movie.id)
            } else {
                // It's not a watch later movie, "watch-later" it
                val favoriteMovie = WatchLaterMovieMapper.from(movie)
                moviesRepository.insertWatchLaterMovie(favoriteMovie)
            }
        }
    }

    /**
     * User pressed the share button
     */
    fun onShareTapped() {
        val movieTitle = movieTitle.value ?: ""
        val youtubeURL = videos.value?.firstOrNull()?.key?.let { URLUtils.getYoutubeURL(it) } ?: ""
        shareMovie.value = Pair(movieTitle, youtubeURL)
    }
    //endregion

    private fun map(movie: Movie): MovieDetailsUIModel {
        val posterURL = movie.posterPath?.let { url -> URLUtils.getImageURL342(url)}
        val backdropURL = movie.backdropPath?.let { url -> URLUtils.getImageURL780(url)}
        val releaseDate = DateUtils.formatDate(movie.releaseDate)
        val voteAverage = movie.voteAverage.toString()
        return MovieDetailsUIModel(posterURL, movie.overview, releaseDate, movie.id, movie.title, backdropURL, voteAverage)
    }

    // Updates the live data streams with the UI model data
    private fun handleUiModel(movie: MovieDetailsUIModel?) {
        backdropURL.value = movie?.backdropPath
        movieTitle.value = movie?.title
        releaseDate.value = movie?.releaseDate
        rating.value = movie?.voteAverage
        posterURL.value = movie?.posterPath
        summary.value = movie?.overview
    }

    // Fetch other movie data from network or db
    private fun fetchData(movieID: Int?) {
        movieID ?: return
        moviesRepository.getFavoriteMovie(movieID).observeForever {
            isFavoriteMovie.value = it != null
        }

        moviesRepository.getWatchLaterMovie(movieID).observeForever {
            isWatchLaterMovie.value = it != null
        }

        moviesRepository
                .getMovieVideos(movieID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    videos.value = result.results
                }, { error ->
                    videosError.value = error
                    videos.value = null
                })

        moviesRepository
                .getMovieCredits(movieID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    cast.value = result.cast
                }, { error ->
                    castError.value = error
                    cast.value = null
                })

        moviesRepository
                .getMovieReviews(movieID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    reviews.value = result.results
                }, { error ->
                    reviewsError.value = error
                    reviews.value = null
                })
    }
}