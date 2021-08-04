package com.asmat.rolando.nightwing.popular_movies

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.asmat.rolando.nightwing.database.NightwingDatabase
import com.asmat.rolando.nightwing.database.entities.PopularMovie
import com.asmat.rolando.nightwing.database.entities.toPopularMovie
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesRemoteMediator(
    private val database: NightwingDatabase,
    private val theMovieDBClient: TheMovieDBClient
) : RemoteMediator<Int, PopularMovie>() {

    val moviesDao = database.moviesDAO()

    // The page that a movie ID belongs to
    val keysTable = mutableMapOf<Long, RemoteKeys>()

    class RemoteKeys(val previousKey: Int?, val nextKey: Int?)

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PopularMovie>
    ): MediatorResult {
        Log.v("RAA", "Load type: $loadType")
        val numOfPages = state.pages.count()
        Log.v("RAA", "Num of pages: $numOfPages")
        var numOfItems = 0
        state.pages.forEach {
            numOfItems += it.data.count()
        }
        Log.v("RAA", "Num of numOfItems: $numOfItems")

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.previousKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }


        try {
            Log.v("RAA", "Fetching page: $page")
            Log.v("RAA", "\n")
            val movies = fetchPage(page)!!

            val endOfPaginationReached = movies.isEmpty()
            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
//                    database.remoteKeysDao().clearRemoteKeys()
                    keysTable.clear()
                    database.moviesDAO().clearAllPopularMovies()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = movies.map {
                    val remoteKeys = RemoteKeys(previousKey = prevKey, nextKey = nextKey)
                    keysTable[it.id] = remoteKeys
                }
//                database.remoteKeysDao().insertAll(keys)
                database.moviesDAO().insertAllPopularMovies(movies)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private fun getNextPageKey(state: PagingState<Int, PopularMovie>) : RemoteKeys? {
        return state.pages.lastOrNull(){ it.data.isNotEmpty() }?.data?.lastOrNull()?.id?.let { lastMovieId ->
            keysTable[lastMovieId]
        }
    }
    private fun getRemoteKeyForFirstItem(state: PagingState<Int, PopularMovie>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                keysTable[repo.id]
            }
    }

    private suspend fun fetchPage(page: Int): List<PopularMovie>? {
        return theMovieDBClient.getPopularMoviesSuspend(page).body()?.results?.map {
            it.toPopularMovie()
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PopularMovie>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                // Get the remote keys of the last item retrieved
//                repoDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
                keysTable[movie.id]
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PopularMovie>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
//                repoDatabase.remoteKeysDao().remoteKeysRepoId(repoId)
                keysTable[id]
            }
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}