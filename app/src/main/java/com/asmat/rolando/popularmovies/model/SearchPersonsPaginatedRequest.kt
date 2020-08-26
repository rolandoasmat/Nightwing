package com.asmat.rolando.popularmovies.model

import com.asmat.rolando.popularmovies.networking.TheMovieDBClient
import com.asmat.rolando.popularmovies.networking.models.PersonsResponse
import io.reactivex.Scheduler
import io.reactivex.Single

data class SearchPersonsPaginatedRequest(
        private val tmdbClient: TheMovieDBClient,
        private val computationScheduler: Scheduler,
        override val mainThreadScheduler: Scheduler): PaginatedRequest<PersonsResponse.Person>(mainThreadScheduler){

    private var searchTerm = ""

    override val shouldLoad: Boolean get() { return this.searchTerm.isNotEmpty()}

    fun setSearchTerm(newSearchTerm: String) {
        searchTerm = newSearchTerm
    }

    override fun fetchData(pageToLoad: Int): Single<PagedData<PersonsResponse.Person>> {
        return tmdbClient
                .searchPerson(searchTerm, pageToLoad)
                .subscribeOn(computationScheduler)
                .map { PagedData(it.results ?: emptyList(), it.total_pages ?: 0) }
    }

    override fun reset() {
        super.reset()
        searchTerm = ""
    }

}