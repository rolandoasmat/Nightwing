package com.asmat.rolando.nightwing.search

import com.asmat.rolando.nightwing.model.PagedData
import com.asmat.rolando.nightwing.model.PaginatedRequest
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import com.asmat.rolando.nightwing.networking.models.TvShowsResponse
import com.asmat.rolando.nightwing.repositories.SchedulersProvider
import io.reactivex.Single

open class SearchTvShowsPaginatedRequest(
        private val tmdbClient: TheMovieDBClient,
        private val schedulersProvider: SchedulersProvider) : PaginatedRequest<TvShowsResponse.Item>(schedulersProvider.mainScheduler){

    private var searchTerm = ""

    override val shouldLoad: Boolean get() { return this.searchTerm.isNotEmpty()}

    fun setSearchTerm(newSearchTerm: String) {
        searchTerm = newSearchTerm
    }

    override fun fetchData(pageToLoad: Int): Single<PagedData<TvShowsResponse.Item>> {
        return tmdbClient
                .searchTvShow(searchTerm, pageToLoad)
                .subscribeOn(schedulersProvider.ioScheduler)
                .map {
                    PagedData(it.results ?: emptyList(), it.total_pages ?: 0)
                }
    }

    override fun reset() {
        super.reset()
        searchTerm = ""
    }

}