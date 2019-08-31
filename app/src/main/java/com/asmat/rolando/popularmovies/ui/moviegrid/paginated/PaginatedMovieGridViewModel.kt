package com.asmat.rolando.popularmovies.ui.moviegrid.paginated

import androidx.lifecycle.MutableLiveData
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

    override val movies = MutableLiveData<List<Movie>>()

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
        super.load()
        init()
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

    private fun init() {
        paginatedRequest.data.removeObserver {  }
        paginatedRequest.data.observeForever { movieResponses ->
            val mapped = movieResponses?.map { MovieMapper.from(it) }
            movies.value = mapped
        }
    }

}