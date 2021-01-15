package com.asmat.rolando.nightwing.repositories

import com.asmat.rolando.nightwing.database.DatabaseRepository
import com.asmat.rolando.nightwing.database.entities.SavedTvShow
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowsRepository @Inject constructor(
        private val tmdbClient: TheMovieDBClient,
        private val schedulersProvider: SchedulersProvider,
        private val databaseRepository: DatabaseRepository) {

    fun getPopularTvShows(page: Int) = tmdbClient.getPopularTvShows(page).subscribeOn(schedulersProvider.ioScheduler)

    fun getTopRatedTvShows(page: Int) = tmdbClient.getTopRatedTvShows(page).subscribeOn(schedulersProvider.ioScheduler)

    fun getOnTheAirTvShows(page: Int) = tmdbClient.getOnTheAirTvShows(page).subscribeOn(schedulersProvider.ioScheduler)

    fun getTvShowDetails(id: Int) = tmdbClient.getTvShowDetails(id).subscribeOn(schedulersProvider.ioScheduler)

    suspend fun deleteSavedTvShow(id: Int) = databaseRepository.deleteSavedTvShow(id)

    suspend fun insertSavedTvShow(data: SavedTvShow) = databaseRepository.insertSavedTvShow(data)

    fun isSavedTvShow(id: Int): Flow<Boolean> = databaseRepository.getSavedTvShow(id).map { it != null }

    fun getAllSavedTvShows() = databaseRepository.getAllSavedTvShows()

}