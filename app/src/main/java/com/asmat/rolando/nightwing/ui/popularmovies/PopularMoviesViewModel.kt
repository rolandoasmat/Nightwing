package com.asmat.rolando.nightwing.ui.popularmovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.asmat.rolando.nightwing.model.mappers.DataModelMapper
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import kotlinx.coroutines.flow.map

class PopularMoviesViewModel(
    moviesRepository: MoviesRepository,
    uiModelMapper: UiModelMapper,
    dataModelMapper: DataModelMapper
) : ViewModel() {

    val flow = moviesRepository
        .popularMoviesPager()
        .flow
        .map { pagingData ->
            pagingData.map { movie ->
                uiModelMapper.mapToGridUiModel(movie)
            }
        }.cachedIn(viewModelScope)


}