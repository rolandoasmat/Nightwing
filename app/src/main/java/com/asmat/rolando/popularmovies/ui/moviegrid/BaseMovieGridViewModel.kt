package com.asmat.rolando.popularmovies.ui.moviegrid

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.model.mappers.UiModelMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository

/**
 * ViewModel of a movie grid
 */
abstract class BaseMovieGridViewModel(val moviesRepository: MoviesRepository,
                                      private val uiModelMapper: UiModelMapper) : ViewModel()  {

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
    val moviesUIModels = MutableLiveData<List<MovieGridItemUiModel>>()

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
    @CallSuper
    open fun load() {
        movies.observeForever {
            moviesUIModels.value = uiModelMapper.map(it)
        }
    }

    /**
     * A movie grid item was pressed
     */
    fun itemPressed(index: Int) {
        movies.value?.get(index)?.let { data ->
            val event = NavigationEvent.ShowMovieDetailScreen(data.id)
            navigationEvent.value = event
        }
    }

    /**
     * Navigation events
     */
    sealed class NavigationEvent {
        // Navigate to the Movie details screen
        data class ShowMovieDetailScreen(val movieID: Int): NavigationEvent()
    }
}