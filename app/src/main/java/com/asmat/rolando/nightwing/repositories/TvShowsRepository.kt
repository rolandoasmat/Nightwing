package com.asmat.rolando.nightwing.repositories

import com.asmat.rolando.nightwing.database.DatabaseRepository
import com.asmat.rolando.nightwing.database.entities.SavedTvShow
import com.asmat.rolando.nightwing.model.Resource
import com.asmat.rolando.nightwing.model.TvShowSummary
import com.asmat.rolando.nightwing.networking.NetworkBoundResource
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import com.asmat.rolando.nightwing.networking.models.TvShowsResponse
import com.asmat.rolando.nightwing.search.SearchTvShowsPaginatedRequest
import com.asmat.rolando.nightwing.tv_season_details.domain.TvShowSeason
import com.asmat.rolando.nightwing.tv_season_details.domain.toTvShowSeason
import com.asmat.rolando.nightwing.tv_season_details.network.TvSeasonDetailsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowsRepository @Inject constructor(
        private val tmdbClient: TheMovieDBClient,
        private val schedulersProvider: SchedulersProvider,
        private val databaseRepository: DatabaseRepository) {

    val searchTvShowsPaginatedRequest = SearchTvShowsPaginatedRequest(tmdbClient, schedulersProvider )

    fun getPopularTvShows(page: Int) = tmdbClient.getPopularTvShows(page).subscribeOn(schedulersProvider.ioScheduler)

    fun popularTvShowsSinglePage() = object: NetworkBoundResource<List<TvShowSummary>>(null) {
        override suspend fun fetchData(): NetworkResponse<List<TvShowSummary>> {
            TODO("Not yet implemented")
        }
    }.load()

    fun getTopRatedTvShows(page: Int) = tmdbClient.getTopRatedTvShows(page).subscribeOn(schedulersProvider.ioScheduler)

    fun getOnTheAirTvShows(page: Int) = tmdbClient.getOnTheAirTvShows(page).subscribeOn(schedulersProvider.ioScheduler)

    fun getTvShowDetails(id: Int) = tmdbClient.getTvShowDetails(id).subscribeOn(schedulersProvider.ioScheduler)

    suspend fun deleteSavedTvShow(id: Int) = databaseRepository.deleteSavedTvShow(id)

    suspend fun insertSavedTvShow(data: SavedTvShow) = databaseRepository.insertSavedTvShow(data)

    fun isSavedTvShow(id: Int): Flow<Boolean> = databaseRepository.getSavedTvShow(id).map { it != null }

    fun getAllSavedTvShows() = databaseRepository.getAllSavedTvShows()

    fun getTvShowSeasonDetails(
        tvShowId: Int,
        seasonNumber: Int): Flow<Resource<TvShowSeason>> {
        return object: NetworkBoundResource<TvShowSeason>(null) {
            override suspend fun fetchData(): NetworkResponse<TvShowSeason> {
                val response = tmdbClient.getTvSeasonDetails(tvShowId, seasonNumber)
                return response.body()?.toTvShowSeason()?.let { data ->
                    NetworkResponse.Success(data)
                } ?: run {
                    NetworkResponse.Failure<TvShowSeason>(response.message())
                }
            }
        }.load()
    }

}