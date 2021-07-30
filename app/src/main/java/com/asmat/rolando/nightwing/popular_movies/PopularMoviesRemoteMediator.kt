package com.asmat.rolando.nightwing.popular_movies

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.asmat.rolando.nightwing.database.DatabaseRepository
import com.asmat.rolando.nightwing.model.Movie
import com.asmat.rolando.nightwing.networking.TheMovieDBClient

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesRemoteMediator(
    private val database: DatabaseRepository,
    private val theMovieDBClient: TheMovieDBClient
) : RemoteMediator<Int, Movie>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {
        val page = when (loadType) {
            REFRESH -> {
                // initial load

            }
            PREPEND -> {
                // scroll up

            }
            APPEND -> {
                // scroll down

            }

        }
    }
}