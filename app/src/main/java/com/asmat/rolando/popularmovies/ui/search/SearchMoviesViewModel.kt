package com.asmat.rolando.popularmovies.ui.search

import com.asmat.rolando.popularmovies.model.mappers.DataModelMapper
import com.asmat.rolando.popularmovies.model.mappers.UiModelMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridViewModel

class SearchMoviesViewModel(moviesRepository: MoviesRepository,
                            uiModelMapper: UiModelMapper,
                            dataModelMapper: DataModelMapper) : PaginatedMovieGridViewModel(moviesRepository, uiModelMapper, dataModelMapper) {

    override val onlyLoadIfDataIsNull = false

    override val paginatedRequest by lazy { moviesRepository.searchMoviesPaginatedRequest }

    fun searchTermChanged(newSearchTerm: String) {
        paginatedRequest.setSearchTerm(newSearchTerm)
        paginatedRequest.load()
    }

    override fun onCleared() {
        super.onCleared()
        paginatedRequest.reset()
    }

}