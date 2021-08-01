package com.asmat.rolando.nightwing.popular_movies.view

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import com.asmat.rolando.nightwing.networking.models.MoviesResponse

class PopularMoviesPagingSource(
    private val theMovieDBClient: TheMovieDBClient
): PagingSource<Int, MoviesResponse.Movie>() {

    // https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data#pagingsource
    override fun getRefreshKey(state: PagingState<Int, MoviesResponse.Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesResponse.Movie> {
        return try {
            // Start refresh at page 1 if undefined.
            val pageNumber = params.key ?: 1
            val response = theMovieDBClient.getPopularMoviesSuspend(pageNumber)
            response.body()?.let { data ->
                val nextPageNumber = if (data.total_pages == pageNumber) {
                    null
                } else {
                    pageNumber + 1
                }
                LoadResult.Page(
                    data = data.results ?: emptyList(),
                    prevKey = null, // Only paging forward.
                    nextKey = nextPageNumber)
            } ?: run {
                val errorMessage = response.message()
                LoadResult.Error(Throwable(errorMessage))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}