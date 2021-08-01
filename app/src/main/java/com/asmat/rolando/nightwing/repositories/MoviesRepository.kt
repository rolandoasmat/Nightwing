package com.asmat.rolando.nightwing.repositories


import com.asmat.rolando.nightwing.database.DatabaseRepository
import com.asmat.rolando.nightwing.database.entities.SavedMovie
import com.asmat.rolando.nightwing.model.*
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import com.asmat.rolando.nightwing.networking.models.*
import com.asmat.rolando.nightwing.ui.recommended_movies.RecommendedMoviesPaginatedRequest
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Used by ViewModels to access movie related data sources
 */
@Singleton
open class MoviesRepository @Inject constructor(
        private val databaseRepository: DatabaseRepository,
        private val tmdbClient: TheMovieDBClient,
        private val schedulersProvider: SchedulersProvider) {

    fun isSavedMovie(id: Int): Flow<Boolean> = databaseRepository.getSavedMovie(id).map { it != null }

    suspend fun setSavedMovie(movie: SavedMovie) = databaseRepository.insertSavedMovie(movie)

    suspend fun clearSavedMovie(id: Int) = databaseRepository.deleteSavedMovie(id)

    fun getSavedMovies() = databaseRepository.getSavedMovies()

    /**
     * Network
     */

    val popularMoviesPaginatedRequest = PopularMoviesPaginatedRequest(tmdbClient, schedulersProvider.ioScheduler, schedulersProvider.mainScheduler)
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

    fun getSimilarMovies(movieID: Int) = tmdbClient.getSimilarMovies(movieID).subscribeOn(schedulersProvider.ioScheduler)

    fun getMovieRecommendations(movieID: Int) = tmdbClient.getMovieRecommendations(movieID).subscribeOn(schedulersProvider.ioScheduler)

    companion object {
        private const val POPULAR_MOVIES_PAGE_SIZE = 20
    }
}