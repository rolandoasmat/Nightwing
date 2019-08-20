package com.asmat.rolando.popularmovies.ui.common

import androidx.lifecycle.Transformations
import com.asmat.rolando.popularmovies.model.PaginatedRequest
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse

/**
 * Base view model class for a grid of Movie items
 */
abstract class PaginatedMovieGridViewModel : BaseMovieGridViewModel()  {

    override val movies by lazy {
        Transformations.map(paginatedRequest.data) { movies ->
            map(movies)
        }
    }
    val loading by lazy { paginatedRequest.loading }
    val loadingMore by lazy { paginatedRequest.loadingMore }
    override val error by lazy { paginatedRequest.error }
    val errorLoadingMore by lazy { paginatedRequest.errorLoadingMore }

    /**
     * Paginated data source
     */
    abstract val paginatedRequest: PaginatedRequest<MoviesResponse.Movie>

    //region API

    /**
     * Load first page of movies
     */
    override fun load() {
        paginatedRequest.load()
    }

    override fun getMovieAt(index: Int): MoviesResponse.Movie? {
        return paginatedRequest.getItem(index)
    }

    /**
     * Load next page of movies
     */
    fun loadMore() {
        paginatedRequest.loadMore()
    }
    //endregion

}