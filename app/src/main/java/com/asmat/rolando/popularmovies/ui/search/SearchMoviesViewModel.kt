package com.asmat.rolando.popularmovies.ui.search

import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridViewModel

class SearchMoviesViewModel(moviesRepository: MoviesRepository) : PaginatedMovieGridViewModel(moviesRepository) {

    override val paginatedRequest by lazy { moviesRepository.searchMoviesPaginatedRequest }

    fun searchTermChanged(newSearchTerm: String) {
        paginatedRequest.setSearchTerm(newSearchTerm)
        paginatedRequest.load()
    }

    override fun onCleared() {
        super.onCleared()
        paginatedRequest.reset()
    }

}