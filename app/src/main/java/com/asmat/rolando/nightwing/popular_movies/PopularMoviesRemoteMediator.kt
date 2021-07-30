package com.asmat.rolando.nightwing.popular_movies

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.asmat.rolando.nightwing.database.DatabaseRepository
import com.asmat.rolando.nightwing.database.entities.PopularMovie
import com.asmat.rolando.nightwing.database.entities.toPopularMovie
import com.asmat.rolando.nightwing.model.Movie
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
        val page = state.pages.size
        val popularMovies = theMovieDBClient.getPopularMoviesSuspend(page).body()?.results?.map {
            it.toPopularMovie()
        }
        database.insertPopularMovies(popularMovies ?: emptyList())
        return MediatorResult.Success(endOfPaginationReached = false)
    }
}