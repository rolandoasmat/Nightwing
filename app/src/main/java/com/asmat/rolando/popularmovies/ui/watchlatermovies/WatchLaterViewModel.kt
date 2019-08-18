package com.asmat.rolando.popularmovies.ui.watchlatermovies

import com.asmat.rolando.popularmovies.model.PaginatedRequest
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.common.MovieGridViewModel

class WatchLaterViewModel(private val moviesRepository: MoviesRepository) : MovieGridViewModel() {

    override val paginatedRequest: PaginatedRequest<MoviesResponse.Movie>
        get() = moviesRepository.popularMoviesPaginatedRequest

}