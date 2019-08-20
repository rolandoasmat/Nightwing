package com.asmat.rolando.popularmovies.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailsUIModel
import com.asmat.rolando.popularmovies.utilities.URLUtils

/**
 * ViewModel of a movie grid
 */
abstract class BaseMovieGridViewModel : ViewModel()  {

    /**
     * Navigation event from a movie grid
     */
    val navigationEvent = MutableLiveData<NavigationEvent>()

    /**
     * Movies data source
     */
    abstract val movies: LiveData<List<Movie>>

    /**
     * UI models of movie grids
     */
    val moviesUIModels: LiveData<List<MovieGridItemUiModel>> by lazy {
        Transformations.map(movies) {
            map(it)
        }
    }

    /**
     * Loading movies
     */
    abstract val loading: LiveData<Boolean>

    /**
     * Error fetching movies
     */
    abstract val error: LiveData<Throwable>

    /**
     * Load movies
     */
    abstract fun load()

    /**
     * An movie grid item was pressed
     */
    fun itemPressed(index: Int) {
        movies.value?.get(index)?.let { data ->
            val uiModel = map(data)
            val event = NavigationEvent.ShowMovieDetailScreen(uiModel)
            navigationEvent.value = event
        }
    }

    protected fun map(movies: List<Movie>): List<MovieGridItemUiModel> {
        return movies.map {
            val posterURL = it.posterPath?.let { url -> URLUtils.getImageURL342(url)}
            MovieGridItemUiModel(it.title, posterURL) }
    }

    protected fun map(movie: Movie): MovieDetailsUIModel {
        val posterURL = movie.posterPath?.let { url -> URLUtils.getImageURL342(url)}
        val backdropURL = movie.backdropPath?.let { url -> URLUtils.getImageURL780(url)}
        return MovieDetailsUIModel(posterURL, movie.overview, movie.releaseDate, movie.id, movie.title, backdropURL, movie.voteAverage)
    }

    /**
     * Navigation events
     */
    sealed class NavigationEvent {
        // Navigate to the Movie details screen
        class ShowMovieDetailScreen(val uiModel: MovieDetailsUIModel): NavigationEvent()
    }
}