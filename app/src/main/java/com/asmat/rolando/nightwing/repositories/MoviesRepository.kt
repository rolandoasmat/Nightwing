package com.asmat.rolando.nightwing.repositories


import com.asmat.rolando.nightwing.database.DatabaseRepository
import com.asmat.rolando.nightwing.model.*
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import com.asmat.rolando.nightwing.networking.models.*
import io.reactivex.Single
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

    /**
     * DB
     */

    fun getSavedMovies() = databaseRepository.getMovies()



    /**
     * Network
     */

    val popularMoviesPaginatedRequest = PopularMoviesPaginatedRequest(tmdbClient, schedulersProvider.ioScheduler, schedulersProvider.mainScheduler)
    val topRatedPaginatedRequest = TopRatedPaginatedRequest(tmdbClient, schedulersProvider.ioScheduler, schedulersProvider.mainScheduler)
    val nowPlayingPaginatedRequest = NowPlayingPaginatedRequest(tmdbClient, schedulersProvider.ioScheduler, schedulersProvider.mainScheduler)
    val upcomingPaginatedRequest = UpcomingPaginatedRequest(tmdbClient, schedulersProvider.ioScheduler, schedulersProvider.mainScheduler)
    private val searchMoviesPaginatedRequest = SearchMoviesPaginatedRequest(tmdbClient, schedulersProvider.ioScheduler, schedulersProvider.mainScheduler)

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

}