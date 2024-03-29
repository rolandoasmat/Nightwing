package com.asmat.rolando.nightwing.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.asmat.rolando.nightwing.database.DatabaseRepository
import com.asmat.rolando.nightwing.database.NightwingDatabase
import com.asmat.rolando.nightwing.database.entities.PopularMovie
import com.asmat.rolando.nightwing.database.entities.SavedMovie
import com.asmat.rolando.nightwing.model.*
import com.asmat.rolando.nightwing.model.mappers.MovieMapper
import com.asmat.rolando.nightwing.networking.NetworkBoundResource
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import com.asmat.rolando.nightwing.networking.models.*
import com.asmat.rolando.nightwing.popular_movies.view.PopularMoviesRemoteMediator
import com.asmat.rolando.nightwing.ui.recommended_movies.RecommendedMoviesPaginatedRequest
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Used by ViewModels to access movie related data sources
 */
@OptIn(ExperimentalPagingApi::class)
@Singleton
open class MoviesRepository @Inject constructor(
        private val databaseRepository: DatabaseRepository,
        private val tmdbClient: TheMovieDBClient,
        private val schedulersProvider: SchedulersProvider,
        private val database: NightwingDatabase,
        private val movieMapper: MovieMapper
) {

    fun popularMoviesPagination(): Flow<PagingData<PopularMovie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false),
            remoteMediator = PopularMoviesRemoteMediator(database, tmdbClient),
            pagingSourceFactory = {
                database.moviesDAO().popularMoviesPagingSource()
            }
        ).flow
    }

    fun popularMoviesSinglePage(): Flow<Resource<List<MovieSummary>>> {
        return object: NetworkBoundResource<List<MovieSummary>>(null) {
            override suspend fun fetchData(): NetworkResponse<List<MovieSummary>> {
                val response = tmdbClient.getPopularMoviesSuspend(1)
                return response.body()?.results?.map {
                    movieMapper.movieResponseToMovieSummary(it)
                }?.let { data ->
                    NetworkResponse.Success(data)
                } ?: run {
                    NetworkResponse.Failure(response.message())
                }
            }
        }.load()
    }

    fun topRatedMoviesSinglePage(): Flow<Resource<List<MovieSummary>>> {
        return object: NetworkBoundResource<List<MovieSummary>>(null) {
            override suspend fun fetchData(): NetworkResponse<List<MovieSummary>> {
                val response = tmdbClient.getTopRatedMoviesSuspend(1)
                return response.body()?.results?.map {
                    movieMapper.movieResponseToMovieSummary(it)
                }?.let { data ->
                    NetworkResponse.Success(data)
                } ?: run {
                    NetworkResponse.Failure(response.message())
                }
            }
        }.load()
    }

    fun nowPlayingMoviesSinglePage(): Flow<Resource<List<MovieSummary>>> {
        return object: NetworkBoundResource<List<MovieSummary>>(null) {
            override suspend fun fetchData(): NetworkResponse<List<MovieSummary>> {
                val response = tmdbClient.getNowPlayingMoviesSuspend(1)
                return response.body()?.results?.map {
                    movieMapper.movieResponseToMovieSummary(it)
                }?.let { data ->
                    NetworkResponse.Success(data)
                } ?: run {
                    NetworkResponse.Failure(response.message())
                }
            }
        }.load()
    }

    fun upcomingMoviesSinglePage(): Flow<Resource<List<MovieSummary>>> {
        return object: NetworkBoundResource<List<MovieSummary>>(null) {
            override suspend fun fetchData(): NetworkResponse<List<MovieSummary>> {
                val response = tmdbClient.getUpcomingMoviesSuspend(1)
                return response.body()?.results?.map {
                    movieMapper.movieResponseToMovieSummary(it)
                }?.let { data ->
                    NetworkResponse.Success(data)
                } ?: run {
                    NetworkResponse.Failure(response.message())
                }
            }
        }.load()
    }

    fun isSavedMovie(id: Int): Flow<Boolean> = databaseRepository.getSavedMovie(id).map { it != null }

    suspend fun setSavedMovie(movie: SavedMovie) = databaseRepository.insertSavedMovie(movie)

    suspend fun clearSavedMovie(id: Int) = databaseRepository.deleteSavedMovie(id)

    fun getSavedMovies() = databaseRepository.getSavedMovies()

    /**
     * Network
     */

    val topRatedPaginatedRequest = TopRatedPaginatedRequest(tmdbClient, schedulersProvider.ioScheduler, schedulersProvider.mainScheduler)
    val nowPlayingPaginatedRequest = NowPlayingPaginatedRequest(tmdbClient, schedulersProvider.ioScheduler, schedulersProvider.mainScheduler)
    val upcomingPaginatedRequest = UpcomingPaginatedRequest(tmdbClient, schedulersProvider.ioScheduler, schedulersProvider.mainScheduler)
    private val searchMoviesPaginatedRequest = SearchMoviesPaginatedRequest(tmdbClient, schedulersProvider.ioScheduler, schedulersProvider.mainScheduler)
    val recommendedMoviesPaginatedRequest = RecommendedMoviesPaginatedRequest(tmdbClient, schedulersProvider)

    open fun movieSearchResultsData() = searchMoviesPaginatedRequest.data
    open fun setMovieSearchQueryText(query: String) = searchMoviesPaginatedRequest.setSearchTerm(query)
    open fun loadMovieSearchResults() = searchMoviesPaginatedRequest.load()
    open fun loadMoreMovieSearchResults() = searchMoviesPaginatedRequest.loadMore()

    fun getMovieDetails(movieID: Int): Single<MovieDetailsResponse> {
        return tmdbClient.getMovieDetails(movieID).subscribeOn(schedulersProvider.ioScheduler)
    }

    fun getMovieVideos(movieID: Int): Single<VideosResponse> {
        return tmdbClient.getMovieVideos(movieID).subscribeOn(schedulersProvider.ioScheduler)
    }

    fun getMovieReviews(movieID: Int): Single<ReviewsResponse> {
        return tmdbClient.getMovieReviews(movieID).subscribeOn(schedulersProvider.ioScheduler)
    }

    fun getMovieCredits(movieID: Int): Single<CreditsResponse> {
        return tmdbClient.getMovieCredits(movieID).subscribeOn(schedulersProvider.ioScheduler)
    }

    fun getSimilarMovies(movieID: Int): Flow<Resource<List<MovieSummary>>> {
        return object: NetworkBoundResource<List<MovieSummary>>(null) {
            override suspend fun fetchData(): NetworkResponse<List<MovieSummary>> {
                val response = tmdbClient.getSimilarMoviesSuspend(movieID)
                return response.body()?.results?.map {
                    movieMapper.movieResponseToMovieSummary(it)
                }?.let { data ->
                    NetworkResponse.Success(data)
                } ?: run {
                    NetworkResponse.Failure(response.message())
                }
            }
        }.load()
    }

    fun getMovieRecommendations(movieID: Int): Flow<Resource<List<MovieSummary>>> {
        return object: NetworkBoundResource<List<MovieSummary>>(null) {
            override suspend fun fetchData(): NetworkResponse<List<MovieSummary>> {
                val response = tmdbClient.getMovieRecommendationsSuspend(movieID)
                return response.body()?.results?.map {
                    movieMapper.movieResponseToMovieSummary(it)
                }?.let { data ->
                    NetworkResponse.Success(data)
                } ?: run {
                    NetworkResponse.Failure(response.message())
                }
            }
        }.load()
    }

    companion object {
        private const val POPULAR_MOVIES_PAGE_SIZE = 20
    }
}