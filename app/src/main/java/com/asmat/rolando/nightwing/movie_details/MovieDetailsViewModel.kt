package com.asmat.rolando.nightwing.movie_details

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.nightwing.deep_links.DeepLinksUtils
import com.asmat.rolando.nightwing.model.mappers.DataModelMapper
import com.asmat.rolando.nightwing.networking.models.CreditsResponse
import com.asmat.rolando.nightwing.networking.models.MovieDetailsResponse
import com.asmat.rolando.nightwing.networking.models.ReviewsResponse
import com.asmat.rolando.nightwing.networking.models.VideosResponse
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.repositories.PeopleRepository
import com.asmat.rolando.nightwing.utilities.DateUtils
import com.asmat.rolando.nightwing.utilities.URLUtils
import io.reactivex.Scheduler

/**
 * Movie Details Screen view model
 */
@SuppressLint("CheckResult")
class MovieDetailsViewModel(
        private val moviesRepository: MoviesRepository,
        private val peopleRepository: PeopleRepository,
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
    val director = MutableLiveData<String>()

    val isFavoriteMovie = MutableLiveData<Boolean>()
    val isWatchLaterMovie = MutableLiveData<Boolean>()
    val shareMovie = MutableLiveData<String>()

    val videos = MutableLiveData<List<VideosResponse.Video>>()
    val videosError = MutableLiveData<Throwable>()

    val cast = MutableLiveData<List<CreditsResponse.Cast>>()
    val castError = MutableLiveData<Throwable>()

    private val _similarMovies = MutableLiveData<List<MovieCardUIModel>>()
    val similarMovies: LiveData<List<MovieCardUIModel>>
        get() { return _similarMovies }

    private val _recommendedMovies = MutableLiveData<List<MovieCardUIModel>>()
    val recommendedMovies: LiveData<List<MovieCardUIModel>>
        get() { return _recommendedMovies }

    private val _directorMovies = MutableLiveData<List<MovieCardUIModel>>()
    val directorMovies: LiveData<List<MovieCardUIModel>>
        get() { return _directorMovies }

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
        val shareText = "Check out $movieTitle!\n$deepLink"
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
                    result.crew.find { it.job ==  DIRECTOR }?.let { director ->
                        this.director.value = director.name
                        getDirectorCredits(director.id)
                    }
                }, { error ->
                    castError.value = error
                    cast.value = null
                })

        moviesRepository
                .getSimilarMovies(movieID)
                .observeOn(mainThreadScheduler)
                .subscribe({ result ->
                    val movies = result.results?.map {
                        val posterURL = it.poster_path?.let { url -> URLUtils.getImageURL342(url)}
                        MovieCardUIModel(it.id ?: 0, posterURL ?: "", it.title ?: "")
                    }
                    _similarMovies.value = movies
                }, { error ->
                    // TODO show error
                })

        moviesRepository
                .getMovieRecommendations(movieID)
                .observeOn(mainThreadScheduler)
                .subscribe({ result ->
                    val movies = result.results?.map {
                        val posterURL = it.poster_path?.let { url -> URLUtils.getImageURL342(url)}
                        MovieCardUIModel(it.id ?: 0,posterURL ?: "", it.title ?: "")
                    }
                    _recommendedMovies.value = movies
                }, { error ->
                    // TODO show error
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

    private fun getDirectorCredits(personID: Int) {
        peopleRepository
                .getPersonMovieCredits(personID)
                .observeOn(mainThreadScheduler)
                .subscribe({ result ->
                    val directorCredits = result.crew?.filter { it.job == DIRECTOR }
                    val movies = directorCredits?.map {
                        val posterURL = it.poster_path?.let { url -> URLUtils.getImageURL342(url)}
                        MovieCardUIModel(it.id ?: 0,posterURL ?: "", it.title ?: "")
                    }
                    _directorMovies.value = movies
                }, { error ->
                    // TODO show error
                })
    }

    companion object {
        private const val DIRECTOR = "Director"
    }
}