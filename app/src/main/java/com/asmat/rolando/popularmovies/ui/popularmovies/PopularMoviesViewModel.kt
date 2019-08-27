package com.asmat.rolando.popularmovies.ui.popularmovies

import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridViewModel
import javax.inject.Inject

class PopularMoviesViewModel @Inject constructor(
        moviesRepository: MoviesRepository
) : PaginatedMovieGridViewModel(moviesRepository) {

    override val paginatedRequest by lazy { moviesRepository.popularMoviesPaginatedRequest }

}