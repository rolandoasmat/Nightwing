package com.asmat.rolando.popularmovies.repositories

import com.asmat.rolando.popularmovies.model.SearchPersonsPaginatedRequest
import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.PersonDetailsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.PersonMovieCredits
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

open class PeopleRepository(
        private val tmdbClient: TheMovieDBClient,
        backgroundScheduler: Scheduler,
        mainThreadScheduler: Scheduler) {

    private val searchPersonsPaginatedRequest = SearchPersonsPaginatedRequest(tmdbClient, backgroundScheduler, mainThreadScheduler)

    fun personsSearchResultsData() = searchPersonsPaginatedRequest.data
    fun setPersonsSearchQueryText(query: String) = searchPersonsPaginatedRequest.setSearchTerm(query)
    fun loadPersonsSearchResults() = searchPersonsPaginatedRequest.load()
    fun loadMorePersonsSearchResults() = searchPersonsPaginatedRequest.loadMore()

    fun getPersonDetails(id: Int): Single<PersonDetailsResponse> {
        return tmdbClient.getPersonDetails(id).subscribeOn(Schedulers.io())
    }

    fun getPersonMovieCredits(id: Int) : Single<PersonMovieCredits> {
        return tmdbClient.getPersonMovieCredits(id).subscribeOn(Schedulers.io())
    }

}