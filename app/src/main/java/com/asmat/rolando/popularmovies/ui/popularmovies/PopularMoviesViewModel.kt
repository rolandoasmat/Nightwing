package com.asmat.rolando.popularmovies.ui.popularmovies

import com.asmat.rolando.popularmovies.model.mappers.DataModelMapper
import com.asmat.rolando.popularmovies.model.mappers.UiModelMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridViewModel

class PopularMoviesViewModel(moviesRepository: MoviesRepository,
                             uiModelMapper: UiModelMapper,
                             dataModelMapper: DataModelMapper) : PaginatedMovieGridViewModel(moviesRepository, uiModelMapper, dataModelMapper) {

    override val paginatedRequest by lazy { moviesRepository.popularMoviesPaginatedRequest }

}