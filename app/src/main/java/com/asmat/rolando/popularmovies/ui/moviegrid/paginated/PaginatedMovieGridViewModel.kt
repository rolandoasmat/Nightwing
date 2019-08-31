package com.asmat.rolando.popularmovies.ui.moviegrid.paginated

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.model.PaginatedRequest
import com.asmat.rolando.popularmovies.model.mappers.MovieMapper
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.moviegrid.BaseMovieGridViewModel

/**
 * Paginated movie grid
 */
abstract class PaginatedMovieGridViewModel(moviesRepository: MoviesRepository) : BaseMovieGridViewModel(moviesRepository) {

    override val movies: LiveData<List<Movie>> by lazy {
        Transformations.map(paginatedRequest.data) { movies ->
            movies.map { MovieMapper.from(it) }
        }
    }

    override val loading by lazy { paginatedRequest.loading }
    val loadingMore by lazy { paginatedRequest.loadingMore }
    override val error by lazy { paginatedRequest.error }
    val errorLoadingMore by lazy { paginatedRequest.errorLoadingMore }

    open val onlyLoadIfDataIsNull = true

    /**
     * Paginated data source
     */
    abstract val paginatedRequest: PaginatedRequest<MoviesResponse.Movie>

    /**
     * Load first page of movies
     */
    override fun load() {
        if (onlyLoadIfDataIsNull) {
            if (paginatedRequest.data.value == null) {
                paginatedRequest.load()
            }
        } else {
            paginatedRequest.load()
        }
    }

    /**
     * Load next page of movies
     */
    fun loadMore() {
        paginatedRequest.loadMore()
    }

}