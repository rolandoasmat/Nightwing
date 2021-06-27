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
import kotlinx.coroutines.flow.map

class PopularMoviesViewModel(
    private val theMovieDBClient: TheMovieDBClient,
    uiModelMapper: UiModelMapper,
    dataModelMapper: DataModelMapper
) : ViewModel() {

    val flow = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(pageSize = POPULAR_MOVIES_PAGE_SIZE)
    ) {
        PopularMoviesPagingSource(theMovieDBClient)
    }.flow
        .map { pagingData ->
            pagingData.map { movie ->
                uiModelMapper.mapToGridUiModel(movie)
            }
        }.cachedIn(viewModelScope)

    companion object {
        private const val POPULAR_MOVIES_PAGE_SIZE = 20
    }
}