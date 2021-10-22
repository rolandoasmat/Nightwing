package com.asmat.rolando.nightwing.ui.moviegrid.paginated

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.asmat.rolando.nightwing.model.Movie
import com.asmat.rolando.nightwing.model.PaginatedRequest
import com.asmat.rolando.nightwing.model.mappers.DataModelMapper
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.networking.models.MoviesResponse
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.ui.moviegrid.BaseMovieGridViewModel
import com.asmat.rolando.nightwing.ui.moviegrid.MovieGridItemUiModel

/**
 * Paginated movie grid
 */
abstract class PaginatedMovieGridViewModel(moviesRepository: MoviesRepository,
                                           private val uiModelMapper: UiModelMapper,
                                           private val dataModelMapper: DataModelMapper) : BaseMovieGridViewModel(moviesRepository, uiModelMapper) {

    private val movies = MutableLiveData<List<Movie>>()

    override val loading by lazy { paginatedRequest.loading }
    val loadingMore by lazy { paginatedRequest.loadingMore }
    override val error by lazy { paginatedRequest.error }
    val errorLoadingMore by lazy { paginatedRequest.errorLoadingMore }

    override val uiModels: LiveData<List<MovieGridItemUiModel>>
        get() = Transformations.map(paginatedRequest.data()) {
            uiModelMapper.mapToGridUiModels(it)
        }

    open val onlyLoadIfDataIsNull = true

    /**
     * Paginated data source
     */
    abstract val paginatedRequest: PaginatedRequest<MoviesResponse.Movie>

    /**
     * Load first page of movies
     */
    override fun load() {
        init()
        if (onlyLoadIfDataIsNull) {
            if (paginatedRequest.data().value == null) {
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
        paginatedRequest.data().removeObserver {  }
        paginatedRequest.data().observeForever { movieResponses ->
            movies.value = dataModelMapper.map(movieResponses)
        }
    }

}