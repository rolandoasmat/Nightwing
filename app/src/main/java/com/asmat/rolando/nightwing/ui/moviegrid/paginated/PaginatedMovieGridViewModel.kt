package com.asmat.rolando.nightwing.ui.moviegrid.paginated

import androidx.lifecycle.MutableLiveData
import com.asmat.rolando.nightwing.model.Movie
import com.asmat.rolando.nightwing.model.PaginatedRequest
import com.asmat.rolando.nightwing.model.mappers.DataModelMapper
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.networking.models.MoviesResponse
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.ui.moviegrid.BaseMovieGridViewModel

/**
 * Paginated movie grid
 */
abstract class PaginatedMovieGridViewModel(moviesRepository: MoviesRepository,
                                           uiModelMapper: UiModelMapper,
                                           private val dataModelMapper: DataModelMapper) : BaseMovieGridViewModel(moviesRepository, uiModelMapper) {

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
            movies.value = dataModelMapper.map(movieResponses)
        }
    }

}