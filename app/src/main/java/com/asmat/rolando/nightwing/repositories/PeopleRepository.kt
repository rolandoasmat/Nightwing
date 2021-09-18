package com.asmat.rolando.nightwing.repositories

import com.asmat.rolando.nightwing.model.*
import com.asmat.rolando.nightwing.networking.NetworkBoundResource
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import com.asmat.rolando.nightwing.networking.models.PersonDetailsResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
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

    fun getPersonMovieCredits(id: Int) : Flow<Resource<PersonMovieCredits>> {
        return object: NetworkBoundResource<PersonMovieCredits>(null) {
            override suspend fun fetchData(): NetworkResponse<PersonMovieCredits> {
                val response = tmdbClient.getPersonMovieCredits(id)
                return response.body()?.let { data ->
                    NetworkResponse.Success(data.toDataModel())
                } ?: run {
                    NetworkResponse.Failure(response.message())
                }
            }
        }.load()
    }

}