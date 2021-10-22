package com.asmat.rolando.nightwing.repositories

import com.asmat.rolando.nightwing.model.*
import com.asmat.rolando.nightwing.model.mappers.MovieMapper
import com.asmat.rolando.nightwing.networking.NetworkBoundResource
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import com.asmat.rolando.nightwing.networking.models.PersonDetailsResponse
import com.asmat.rolando.nightwing.utilities.URLUtils
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class PeopleRepository @Inject constructor(
        private val tmdbClient: TheMovieDBClient,
        private val schedulersProvider: SchedulersProvider,
        private val movieMapper: MovieMapper) {

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

    fun getDirectorMovieCredits(id: Int) : Flow<Resource<List<MovieSummary>>> {
        return object: NetworkBoundResource<List<MovieSummary>>(null) {
            override suspend fun fetchData(): NetworkResponse<List<MovieSummary>> {
                val response = tmdbClient.getPersonMovieCredits(id)
                return response.body()?.let { data ->
                    val directorCredits = data.crew?.filter { it.job == DIRECTOR }
                    val movies = directorCredits?.map {
                        val posterURL = it.poster_path?.let { url -> URLUtils.getImageURL342(url)}
                        MovieSummary(
                            id = it.id ?: 0,
                            title = it.title ?: "",
                            posterPath = posterURL ?: "")
                    } ?: emptyList()
                    NetworkResponse.Success(movies)
                } ?: run {
                    NetworkResponse.Failure(response.message())
                }
            }
        }.load()
    }

    fun getActorMovieCredits(id: Int) : Flow<Resource<PersonMovieCredits>> {
        return object: NetworkBoundResource<PersonMovieCredits>(null) {
            override suspend fun fetchData(): NetworkResponse<PersonMovieCredits> {
                val response = tmdbClient.getPersonMovieCredits(id)
                return response.body()?.toDataModel()?.let { data ->
                    NetworkResponse.Success(data)
                } ?: run {
                    NetworkResponse.Failure(response.message())
                }
            }
        }.load()
    }

    companion object {
        private const val DIRECTOR = "Director"
    }

}