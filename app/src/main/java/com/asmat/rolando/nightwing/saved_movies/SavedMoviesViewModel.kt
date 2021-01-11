package com.asmat.rolando.nightwing.saved_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import kotlinx.coroutines.flow.map

class SavedMoviesViewModel(
        moviesRepository: MoviesRepository,
        uiModelMapper: UiModelMapper): ViewModel() {

    val movies = moviesRepository
            .getSavedMovies()
            .map {
                uiModelMapper.mapToGridItems(it)
            }.asLiveData()
}