package com.asmat.rolando.popularmovies.ui.moviegrid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.utilities.URLUtils

/**
 * ViewModel of a movie grid
 */
abstract class BaseMovieGridViewModel(val moviesRepository: MoviesRepository) : ViewModel()  {

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
            moviesRepository.setMovieDetailsData(data)
            val event = NavigationEvent.ShowMovieDetailScreen
            navigationEvent.value = event
        }
    }

    protected fun map(movies: List<Movie>): List<MovieGridItemUiModel> {
        return movies.map {
            val posterURL = it.posterPath?.let { url -> URLUtils.getImageURL342(url)}
            MovieGridItemUiModel(it.title, posterURL)
        }
    }

    /**
     * Navigation events
     */
    sealed class NavigationEvent {
        // Navigate to the Movie details screen
        object ShowMovieDetailScreen: NavigationEvent()
    }
}