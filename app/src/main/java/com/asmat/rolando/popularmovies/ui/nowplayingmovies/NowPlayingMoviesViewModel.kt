package com.asmat.rolando.popularmovies.ui.nowplayingmovies

import com.asmat.rolando.popularmovies.model.PaginatedRequest
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.common.MovieGridViewModel

class NowPlayingMoviesViewModel(private val moviesRepository: MoviesRepository) : MovieGridViewModel() {

    val paginatedRequest: PaginatedRequest<MoviesResponse.Movie>
        get() = moviesRepository.popularMoviesPagedData

}