package com.asmat.rolando.nightwing.movie_details

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.nightwing.deep_links.DeepLinksUtils
import com.asmat.rolando.nightwing.model.mappers.DataModelMapper
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.networking.models.CreditsResponse
import com.asmat.rolando.nightwing.networking.models.ReviewsResponse
import com.asmat.rolando.nightwing.networking.models.VideosResponse
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.repositories.PeopleRepository
import com.asmat.rolando.nightwing.ui.row_view.RowViewItemUiModel
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
        private val mainThreadScheduler: Scheduler,
        private val uiModelMapper: UiModelMapper) : ViewModel() {

    private val _movieDetailsUIModel = MutableLiveData<MovieDetailsUIModel>()
    val movieDetailsUIModel: LiveData<MovieDetailsUIModel>
        get() = _movieDetailsUIModel

    val director = MutableLiveData<String>()

    val isFavoriteMovie = MutableLiveData<Boolean>()
    val isWatchLaterMovie = MutableLiveData<Boolean>()
    val shareMovie = MutableLiveData<String>()

    val videos = MutableLiveData<List<VideosResponse.Video>>()
    val videosError = MutableLiveData<Throwable>()

    val cast = MutableLiveData<List<CreditsResponse.Cast>>()
    val castError = MutableLiveData<Throwable>()

    private val _similarMovies = MutableLiveData<List<RowViewItemUiModel>>()
    val similarMovies: LiveData<List<RowViewItemUiModel>>
        get() { return _similarMovies }

    private val _recommendedMovies = MutableLiveData<List<RowViewItemUiModel>>()
    val recommendedMovies: LiveData<List<RowViewItemUiModel>>
        get() { return _recommendedMovies }

    private val _directorMovies = MutableLiveData<List<RowViewItemUiModel>>()
    val directorMovies: LiveData<List<RowViewItemUiModel>>
        get() { return _directorMovies }

    val reviews = MutableLiveData<List<ReviewsResponse.Review>>()
    val reviewsError = MutableLiveData<Throwable>()

    private var movieID: Int = 0

    //region API

    fun init(movieID: Int) {
        this.movieID = movieID
        fetchData(this.movieID)
    }

    /**
     * User pressed the heart
     */
    fun onSaveTapped() {
//        isFavoriteMovie.value?.let {
//            if (it) {
//                // It's a favorite movie, "un-favorite" it
//                moviesRepository.removeFavoriteMovie(movieID)
//            } else {
//                // It's not a favorite movie, "favorite" it
//                _movieDetailsUIModel.value?.let {
//                    val mapped = dataModelMapper.mapToFavoriteMovie(it)
//                    moviesRepository.insertFavoriteMovie(mapped)
//                }
//            }
//        }
    }

    /**
     * User pressed the share button
     */
    fun onShareTapped() {
        val movieTitle = _movieDetailsUIModel.value?.title ?: ""
        val deepLink = deepLinksUtils.shareMovieDetailsDeepLink(movieID)
        val shareText = "Check out $movieTitle!\n$deepLink"
        shareMovie.value = shareText
    }
    //endregion

    // Fetch other movie data from network or db
    private fun fetchData(movieID: Int) {

        moviesRepository
                .getMovieDetails(movieID)
                .observeOn(mainThreadScheduler)
                .subscribe({
                    val uiModel = uiModelMapper.map(it)
                    _movieDetailsUIModel.value = uiModel
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
                        RowViewItemUiModel(it.id ?: 0, posterURL ?: "", it.title ?: "")
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
                        RowViewItemUiModel(it.id ?: 0,posterURL ?: "", it.title ?: "")
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
                        RowViewItemUiModel(it.id ?: 0,posterURL ?: "", it.title ?: "")
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