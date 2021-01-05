package com.asmat.rolando.nightwing.repositories

import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowsRepository @Inject constructor(
        private val tmdbClient: TheMovieDBClient,
        private val schedulersProvider: SchedulersProvider) {

    fun getPopularTvShows(page: Int) = tmdbClient.getPopularTvShows(page).subscribeOn(schedulersProvider.ioScheduler)

    fun getTopRatedTvShows(page: Int) = tmdbClient.getTopRatedTvShows(page).subscribeOn(schedulersProvider.ioScheduler)

    fun getOnTheAirTvShows(page: Int) = tmdbClient.getOnTheAirTvShows(page).subscribeOn(schedulersProvider.ioScheduler)
}