package com.asmat.rolando.nightwing.ui.moviegrid

import androidx.lifecycle.LiveData
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
}