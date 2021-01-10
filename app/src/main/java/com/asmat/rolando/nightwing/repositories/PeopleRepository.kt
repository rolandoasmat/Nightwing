package com.asmat.rolando.nightwing.repositories

import com.asmat.rolando.nightwing.model.SearchPersonsPaginatedRequest
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import com.asmat.rolando.nightwing.networking.models.PersonDetailsResponse
import com.asmat.rolando.nightwing.networking.models.PersonMovieCredits
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class PeopleRepository @Inject constructor(
        private val tmdbClient: TheMovieDBClient,
        private val schedulersProvider: SchedulersProvider) {

    private val searchPersonsPaginatedRequest = SearchPersonsPaginatedRequest(tmdbClient, schedulersProvider.ioScheduler, schedulersProvider.mainScheduler)

    open fun personsSearchResultsData() = searchPersonsPaginatedRequest.data
    open fun setPersonsSearchQueryText(query: String) = searchPersonsPaginatedRequest.setSearchTerm(query)
    open fun loadPersonsSearchResults() = searchPersonsPaginatedRequest.load()
    fun loadMorePersonsSearchResults() = searchPersonsPaginatedRequest.loadMore()

    fun getPopularPeople(page: Int) = tmdbClient
                .getPopularPeople(page)
                .subscribeOn(schedulersProvider.ioScheduler)

    fun getPersonDetails(id: Int): Single<PersonDetailsResponse> {
        return tmdbClient.getPersonDetails(id).subscribeOn(Schedulers.io())
    }

    fun getPersonMovieCredits(id: Int) : Single<PersonMovieCredits> {
        return tmdbClient.getPersonMovieCredits(id).subscribeOn(Schedulers.io())
    }

}