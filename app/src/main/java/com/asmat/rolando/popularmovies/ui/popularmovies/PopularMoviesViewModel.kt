package com.asmat.rolando.popularmovies.ui.popularmovies

import com.asmat.rolando.popularmovies.model.PaginatedRequest
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.common.PaginatedMovieGridViewModel

class PopularMoviesViewModel(private val moviesRepository: MoviesRepository) : PaginatedMovieGridViewModel() {

    override val paginatedRequest: PaginatedRequest<MoviesResponse.Movie>
        get() = moviesRepository.popularMoviesPaginatedRequest

}