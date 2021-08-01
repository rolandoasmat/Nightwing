package com.asmat.rolando.nightwing.popular_movies

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.asmat.rolando.nightwing.database.DatabaseRepository
import com.asmat.rolando.nightwing.database.entities.PopularMovie
import com.asmat.rolando.nightwing.database.entities.toPopularMovie
import com.asmat.rolando.nightwing.networking.TheMovieDBClient

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesRemoteMediator(
    private val database: DatabaseRepository,
    private val theMovieDBClient: TheMovieDBClient
) : RemoteMediator<Int, PopularMovie>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PopularMovie>
    ): MediatorResult {
        return when (loadType) {
            LoadType.REFRESH -> {
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                fetchPage(1)?.let { movies ->
                    database.clearAllPopularMovies()
                    database.insertAllPopularMovies(movies)
                    MediatorResult.Success(endOfPaginationReached = false)
                } ?: run {
                    MediatorResult.Error(Throwable("Something went wrong."))
                }
            }
            LoadType.PREPEND ->
                MediatorResult.Success(endOfPaginationReached = false)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()

                // You must explicitly check if the last item is null when
                // appending, since passing null to networkService is only
                // valid for initial load. If lastItem is null it means no
                // items were loaded after the initial REFRESH and there are
                // no more items to load.
                if (lastItem == null) {
                    MediatorResult.Success(endOfPaginationReached = true)
                } else {
                    val nextPage = state.pages.count() + 1
                    fetchPage(nextPage)?.let { movies ->
                        database.insertAllPopularMovies(movies)
                        MediatorResult.Success(endOfPaginationReached = false)
                    } ?: run {
                        MediatorResult.Error(Throwable("Something went wrong."))
                    }
                }
            }
        }
    }

    private suspend fun fetchPage(page: Int): List<PopularMovie>? {
        return theMovieDBClient.getPopularMoviesSuspend(page).body()?.results?.map {
            it.toPopularMovie()
        }
    }
}