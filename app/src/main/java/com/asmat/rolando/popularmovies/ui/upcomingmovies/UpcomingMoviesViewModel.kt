package com.asmat.rolando.popularmovies.ui.upcomingmovies

import com.asmat.rolando.popularmovies.model.PaginatedRequest
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridViewModel
import javax.inject.Inject

class UpcomingMoviesViewModel @Inject constructor(
        moviesRepository: MoviesRepository
) : PaginatedMovieGridViewModel(moviesRepository) {

    override val paginatedRequest: PaginatedRequest<MoviesResponse.Movie>
        get() = moviesRepository.upcomingPaginatedRequest

}