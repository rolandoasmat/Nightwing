package com.asmat.rolando.nightwing.popular_movies.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.asmat.rolando.nightwing.database.DatabaseRepository
import com.asmat.rolando.nightwing.database.NightwingDatabase
import com.asmat.rolando.nightwing.database.entities.PopularMovie
import com.asmat.rolando.nightwing.model.mappers.DataModelMapper
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import com.asmat.rolando.nightwing.popular_movies.PopularMoviesRemoteMediator
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.ui.moviegrid.toMovieGridItemUiModel
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesViewModel(
    moviesRepository: MoviesRepository,
    uiModelMapper: UiModelMapper,
    dataModelMapper: DataModelMapper,
    private val database: NightwingDatabase,
    private val tmdbClient: TheMovieDBClient
) : ViewModel() {

    private fun pager(): Pager<Int, PopularMovie> {
        Log.d("RAA", "NEW PAGER")

        return Pager(
            // Configure how data is loaded by passing additional properties to
            // PagingConfig, such as prefetchDistance.
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = PopularMoviesRemoteMediator(database, tmdbClient),
            pagingSourceFactory = { database.moviesDAO().popularMoviesPagingSource() }
        )
    }
    val flow = pager()
        .flow
        .map { pagingData ->
            pagingData.map { movie ->
                movie.toMovieGridItemUiModel()
            }
        }
        .cachedIn(viewModelScope)


}