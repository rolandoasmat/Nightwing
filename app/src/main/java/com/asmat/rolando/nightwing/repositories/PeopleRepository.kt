package com.asmat.rolando.nightwing.repositories

import com.asmat.rolando.nightwing.model.SearchPersonsPaginatedRequest
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import com.asmat.rolando.nightwing.networking.models.PersonDetailsResponse
import com.asmat.rolando.nightwing.networking.models.PersonMovieCredits
import com.asmat.rolando.nightwing.popular_people.PopularPersonUiModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

open class PeopleRepository(
        private val tmdbClient: TheMovieDBClient,
        private val backgroundScheduler: Scheduler,
        private val mainThreadScheduler: Scheduler) {

    private val searchPersonsPaginatedRequest = SearchPersonsPaginatedRequest(tmdbClient, backgroundScheduler, mainThreadScheduler)

    open fun personsSearchResultsData() = searchPersonsPaginatedRequest.data
    open fun setPersonsSearchQueryText(query: String) = searchPersonsPaginatedRequest.setSearchTerm(query)
    open fun loadPersonsSearchResults() = searchPersonsPaginatedRequest.load()
    fun loadMorePersonsSearchResults() = searchPersonsPaginatedRequest.loadMore()

    fun getPopularPeople(page: Int) = tmdbClient
                .getPopularPeople(page)
                .subscribeOn(backgroundScheduler)

    fun getPersonDetails(id: Int): Single<PersonDetailsResponse> {
        return tmdbClient.getPersonDetails(id).subscribeOn(Schedulers.io())
    }

    fun getPersonMovieCredits(id: Int) : Single<PersonMovieCredits> {
        return tmdbClient.getPersonMovieCredits(id).subscribeOn(Schedulers.io())
    }

}