package com.asmat.rolando.popularmovies.ui.moviedetails

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.model.mappers.DataModelMapper
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.CreditsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.ReviewsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.VideosResponse
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.utilities.DateUtils
import com.asmat.rolando.popularmovies.utilities.URLUtils
import io.reactivex.Scheduler

/**
 * Movie Details Screen view model
 */
@SuppressLint("CheckResult")
class MovieDetailsViewModel(private val moviesRepository: MoviesRepository,
                            private val dataModelMapper: DataModelMapper,
                            private val mainThreadScheduler: Scheduler) : ViewModel() {

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

    private var movieID: Int = 0

    //region API

    fun init(movieID: String) {
        this.movieID = movieID.toInt()
        fetchData(this.movieID)
    }

    /**
     * User pressed the star, "favorite movie", icon
     */
    fun onStarTapped() {
        isFavoriteMovie.value?.let {
            if (it) {
                // It's a favorite movie, "un-favorite" it
                moviesRepository.removeFavoriteMovie(movieID)
            } else {
                // It's not a favorite movie, "favorite" it
//                val mapped = dataModelMapper.mapToFavoriteMovie(movie)
//                moviesRepository.insertFavoriteMovie(mapped)
            }
        }
    }

    /**
     * User pressed the bookmark, "watch later movie", icon
     */
    fun onBookmarkTapped() {
        isWatchLaterMovie.value?.let {
            if (it) {
                // It's a watch later movie, "un-watch-later" it
                moviesRepository.removeWatchLaterMovie(movieID)
            } else {
                // It's not a watch later movie, "watch-later" it
//                val mapped = dataModelMapper.mapToWatchLaterMovie(movie)
//                moviesRepository.insertWatchLaterMovie(mapped)
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

    private fun map(movie: Movie?): MovieDetailsUIModel? {
        if (movie == null) return null
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
    private fun fetchData(movieID: Int) {
        moviesRepository.getFavoriteMovie(movieID).observeForever {
            isFavoriteMovie.value = it != null
        }

        moviesRepository.getWatchLaterMovie(movieID).observeForever {
            isWatchLaterMovie.value = it != null
        }

        moviesRepository
                .getMovieVideos(movieID)
                .observeOn(mainThreadScheduler)
                .subscribe({ result ->
                    videos.value = result.results
                }, { error ->
                    videosError.value = error
                    videos.value = null
                })

        moviesRepository
                .getMovieCredits(movieID)
                .observeOn(mainThreadScheduler)
                .subscribe({ result ->
                    cast.value = result.cast
                }, { error ->
                    castError.value = error
                    cast.value = null
                })

        moviesRepository
                .getMovieReviews(movieID)
                .observeOn(mainThreadScheduler)
                .subscribe({ result ->
                    reviews.value = result.results
                }, { error ->
                    reviewsError.value = error
                    reviews.value = null
                })
    }
}