package com.asmat.rolando.popularmovies.repositories

import com.asmat.rolando.popularmovies.model.SearchMoviesPaginatedRequest
import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.PersonDetailsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.PersonMovieCredits
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PeopleRepository(
        private val tmdbClient: TheMovieDBClient,
        backgroundScheduler: Scheduler,
        mainThreadScheduler: Scheduler) {

    val searchPersonsPaginatedRequest = SearchMoviesPaginatedRequest(tmdbClient, backgroundScheduler, mainThreadScheduler)

    fun getPersonDetails(id: Int): Single<PersonDetailsResponse> {
        return tmdbClient.getPersonDetails(id).subscribeOn(Schedulers.io())
    }

    fun getPersonMovieCredits(id: Int) : Single<PersonMovieCredits> {
        return tmdbClient.getPersonMovieCredits(id).subscribeOn(Schedulers.io())
    }

}