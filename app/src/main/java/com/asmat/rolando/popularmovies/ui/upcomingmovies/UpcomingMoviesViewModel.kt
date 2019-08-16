package com.asmat.rolando.popularmovies.ui.upcomingmovies

import com.asmat.rolando.popularmovies.model.PagedData
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.common.MovieGridViewModel

class UpcomingMoviesViewModel(private val moviesRepository: MoviesRepository) : MovieGridViewModel() {

    override val pagedData: PagedData<MoviesResponse.Movie>
        get() = moviesRepository.popularMoviesPagedData

}