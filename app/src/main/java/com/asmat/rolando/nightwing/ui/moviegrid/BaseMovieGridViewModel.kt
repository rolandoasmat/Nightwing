package com.asmat.rolando.nightwing.ui.moviegrid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.MoviesRepository

/**
 * ViewModel of a movie grid
 */
abstract class BaseMovieGridViewModel(
        val moviesRepository: MoviesRepository,
        private val uiModelMapper: UiModelMapper) : ViewModel()  {

    /**
     * Navigation event from a movie grid
     */
    val navigationEvent = MutableLiveData<NavigationEvent>()

    /**
     * UI models of movie grids
     */
    abstract val uiModels: LiveData<List<MovieGridItemUiModel>>

    /**
     * Loading movies
     */
    abstract val loading: LiveData<Boolean>

    /**
     * Error fetching movies
     */
    abstract val error: LiveData<Throwable>

    abstract fun load()

    /**
     * A movie grid item was pressed
     */
    fun itemPressed(index: Int) {
        uiModels.value?.get(index)?.let { data ->
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