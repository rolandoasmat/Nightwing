package com.asmat.rolando.popularmovies.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.model.PaginatedRequest
import com.asmat.rolando.popularmovies.model.mappers.MovieMapper
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.utilities.URLUtils

/**
 * Base view model class for a grid of Movie items
 */
abstract class PaginatedMovieGridViewModel : BaseMovieGridViewModel() {

    override val moviesUIModels: LiveData<List<MovieGridItemUiModel>>
        get() = Transformations.map(paginatedRequest.data) { movies ->
            movies.map {
                val posterURL = it.poster_path?.let { url -> URLUtils.getImageURL342(url) }
                MovieGridItemUiModel(it.title, posterURL)
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

    /**
     * Load next page of movies
     */
    fun loadMore() {
        paginatedRequest.loadMore()
    }
    //endregion

}