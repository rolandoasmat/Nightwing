package com.asmat.rolando.nightwing.ui.upcomingmovies

import com.asmat.rolando.nightwing.model.PaginatedRequest
import com.asmat.rolando.nightwing.model.mappers.DataModelMapper
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.networking.models.MoviesResponse
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.ui.moviegrid.paginated.PaginatedMovieGridViewModel

class UpcomingMoviesViewModel(moviesRepository: MoviesRepository,
                              uiModelMapper: UiModelMapper,
                              dataModelMapper: DataModelMapper) : PaginatedMovieGridViewModel(moviesRepository, uiModelMapper, dataModelMapper) {

    override val paginatedRequest: PaginatedRequest<MoviesResponse.Movie>
        get() = moviesRepository.upcomingPaginatedRequest

}