package com.asmat.rolando.popularmovies.model

import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import io.reactivex.Scheduler
import io.reactivex.Single

data class SearchMoviesPaginatedRequest(private val tmdbClient: TheMovieDBClient,
                                        private val computationScheduler: Scheduler,
                                        override val mainThreadScheduler: Scheduler) : PaginatedRequest<MoviesResponse.Movie>(mainThreadScheduler){

    private var searchTerm = ""

    override val shouldLoad: Boolean get() { return this.searchTerm.isNotEmpty()}

    fun setSearchTerm(newSearchTerm: String) {
        searchTerm = newSearchTerm
    }

    override fun fetchData(pageToLoad: Int): Single<PagedData<MoviesResponse.Movie>> {
        return tmdbClient
                .searchMovie(searchTerm, pageToLoad)
                .subscribeOn(computationScheduler)
                .map { PagedData(it.results ?: emptyList(), it.total_pages ?: 0) }
    }

    override fun reset() {
        super.reset()
        searchTerm = ""
    }

}