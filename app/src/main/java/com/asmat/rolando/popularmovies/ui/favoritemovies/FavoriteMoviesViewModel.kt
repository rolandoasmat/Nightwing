package com.asmat.rolando.popularmovies.ui.favoritemovies

import com.asmat.rolando.popularmovies.model.PagedData
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.common.MovieGridViewModel

class FavoriteMoviesViewModel(private val moviesRepository: MoviesRepository) : MovieGridViewModel() {
    override val pagedData: PagedData<MoviesResponse.Movie>
        get() = moviesRepository.popularMoviesPagedData
}