package com.asmat.rolando.nightwing.ui.recommended_movies

import com.asmat.rolando.nightwing.model.mappers.DataModelMapper
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.ui.moviegrid.paginated.PaginatedMovieGridViewModel

class RecommendedMoviesViewModel(
        moviesRepository: MoviesRepository,
        uiModelMapper: UiModelMapper,
        dataModelMapper: DataModelMapper) : PaginatedMovieGridViewModel(moviesRepository, uiModelMapper, dataModelMapper) {

    override val paginatedRequest by lazy { moviesRepository.popularMoviesPaginatedRequest }

}