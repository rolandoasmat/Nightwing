package com.asmat.rolando.popularmovies.ui.nowplayingmovies

import com.asmat.rolando.popularmovies.model.PaginatedRequest
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridViewModel
import javax.inject.Inject

class NowPlayingMoviesViewModel @Inject constructor(
        moviesRepository: MoviesRepository
) : PaginatedMovieGridViewModel(moviesRepository) {

    override val paginatedRequest: PaginatedRequest<MoviesResponse.Movie>
        get() = moviesRepository.nowPlayingPaginatedRequest

}