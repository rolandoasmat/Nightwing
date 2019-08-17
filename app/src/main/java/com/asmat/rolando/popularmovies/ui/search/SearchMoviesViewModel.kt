package com.asmat.rolando.popularmovies.ui.search

import com.asmat.rolando.popularmovies.model.PaginatedRequest
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.common.MovieGridViewModel

class SearchMoviesViewModel(private val moviesRepository: MoviesRepository) : MovieGridViewModel() {

    private var searchTerm = ""

    val paginatedRequest: PaginatedRequest<MoviesResponse.Movie>
        get() = moviesRepository.popularMoviesPagedData

    fun searchTermChanged(newSearchTerm: String) {
        searchTerm = newSearchTerm
    }

}