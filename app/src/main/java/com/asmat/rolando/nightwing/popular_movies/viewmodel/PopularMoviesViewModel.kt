package com.asmat.rolando.nightwing.popular_movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesViewModel(
    moviesRepository: MoviesRepository,
    uiModelMapper: UiModelMapper
) : ViewModel() {

    val popularMovies = moviesRepository.popularMoviesPagination()
        .map { pagingData ->
            pagingData.map { movie ->
                uiModelMapper.popularMovieToMovieGridItemUiModel(movie)
            }
        }
        .cachedIn(viewModelScope)
        .distinctUntilChanged()
        .asLiveData()

}