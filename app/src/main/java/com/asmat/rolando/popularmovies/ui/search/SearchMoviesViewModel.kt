package com.asmat.rolando.popularmovies.ui.search

import com.asmat.rolando.popularmovies.model.PaginatedRequest
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridViewModel

class SearchMoviesViewModel(private val moviesRepository: MoviesRepository) : PaginatedMovieGridViewModel() {

    private var searchTerm = ""

    override val paginatedRequest: PaginatedRequest<MoviesResponse.Movie>
        get() = moviesRepository.popularMoviesPaginatedRequest

    fun searchTermChanged(newSearchTerm: String) {
        searchTerm = newSearchTerm
    }

}