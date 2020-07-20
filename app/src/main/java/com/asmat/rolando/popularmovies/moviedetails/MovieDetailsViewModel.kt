package com.asmat.rolando.popularmovies.moviedetails

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.deep_links.DeepLinksUtils
import com.asmat.rolando.popularmovies.model.mappers.DataModelMapper
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.CreditsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MovieDetailsResponse
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
class MovieDetailsViewModel(
        private val moviesRepository: MoviesRepository,
        private val dataModelMapper: DataModelMapper,
        private val deepLinksUtils: DeepLinksUtils,
        private val mainThreadScheduler: Scheduler) : ViewModel() {

    val backdropURL = MutableLiveData<String>()
    val movieTitle = MutableLiveData<String>()
    val releaseDate = MutableLiveData<String>()
    val rating = MutableLiveData<String>()
    val runtime = MutableLiveData<String>()
    val posterURL = MutableLiveData<String>()
    val summary = MutableLiveData<String>()
    val tagline = MutableLiveData<String>()

    val isFavoriteMovie = MutableLiveData<Boolean>()
    val isWatchLaterMovie = MutableLiveData<Boolean>()
    val shareMovie = MutableLiveData<String>()

    val videos = MutableLiveData<List<VideosResponse.Video>>()
    val videosError = MutableLiveData<Throwable>()

    val cast = MutableLiveData<List<CreditsResponse.Cast>>()
    val castError = MutableLiveData<Throwable>()

    val reviews = MutableLiveData<List<ReviewsResponse.Review>>()
    val reviewsError = MutableLiveData<Throwable>()

    private var data: MovieDetailsResponse? = null
    set(value) {
        field = value
        value?.let {
            val uiModel = map(value)
            handleUiModel(uiModel)
        }
    }

    private var movieID: Int = 0

    //region API

    fun init(movieID: Int) {
        this.movieID = movieID
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
                data?.let {
                    val mapped = dataModelMapper.mapToFavoriteMovie(it)
                    moviesRepository.insertFavoriteMovie(mapped)
                }
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
                data?.let {
                    val mapped = dataModelMapper.mapToWatchLaterMovie(it)
                    moviesRepository.insertWatchLaterMovie(mapped)
                }

            }
        }
    }

    /**
     * User pressed the share button
     */
    fun onShareTapped() {
        val movieTitle = movieTitle.value ?: ""
        val deepLink = deepLinksUtils.shareMovieDetailsDeepLink(movieID)
        val shareText = "Check out $movieTitle! "
        shareMovie.value = shareText
    }
    //endregion

    private fun map(movie: MovieDetailsResponse): MovieDetailsUIModel? {
        val posterURL = movie.poster_path?.let { url -> URLUtils.getImageURL342(url)}
        val backdropURL = movie.backdrop_path?.let { url -> URLUtils.getImageURL780(url)}
        val releaseDate = DateUtils.formatDate(movie.release_date ?: "")
        val voteAverage = movie.vote_average?.times(10)?.toInt()?.toString()?.let { percent ->
            "$percent%"
        }
        val runtime = movie.runtime?.let { movieRuntime ->
            "$movieRuntime min"
        }
        return MovieDetailsUIModel(posterURL,
                movie.overview ?: "",
                releaseDate,
                movie.id ?: 0,
                movie.title ?: "",
                backdropURL,
                voteAverage,
                runtime,
                movie.tagline)
    }

    // Updates the live data streams with the UI model data
    private fun handleUiModel(movie: MovieDetailsUIModel?) {
        backdropURL.value = movie?.backdropPath
        movieTitle.value = movie?.title
        releaseDate.value = movie?.releaseDate
        rating.value = movie?.voteAverage
        runtime.value = movie?.runtime
        posterURL.value = movie?.posterPath
        summary.value = movie?.overview
        tagline.value = movie?.tagline
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
                .getMovieDetails(movieID)
                .observeOn(mainThreadScheduler)
                .subscribe({
                    data = it
                }, {})

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