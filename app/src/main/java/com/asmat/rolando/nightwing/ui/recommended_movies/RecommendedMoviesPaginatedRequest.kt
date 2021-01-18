package com.asmat.rolando.nightwing.ui.recommended_movies

import com.asmat.rolando.nightwing.model.PagedData
import com.asmat.rolando.nightwing.model.PaginatedRequest
import com.asmat.rolando.nightwing.networking.TheMovieDBClient
import com.asmat.rolando.nightwing.networking.models.MoviesResponse
import com.asmat.rolando.nightwing.repositories.SchedulersProvider
import io.reactivex.Single

data class RecommendedMoviesPaginatedRequest(
        private val tmdbClient: TheMovieDBClient,
        private val schedulersProvider: SchedulersProvider) : PaginatedRequest<MoviesResponse.Movie>(schedulersProvider.mainScheduler){

    private var movieId: Int? = null

    override val shouldLoad: Boolean
    get() {
        return movieId != null
    }

    fun setMovieId(id: Int) {
        movieId = id
    }

    override fun fetchData(pageToLoad: Int): Single<PagedData<MoviesResponse.Movie>> {
        return tmdbClient
                .getMovieRecommendations(movieId ?: 0)
                .subscribeOn(schedulersProvider.ioScheduler)
                .map { PagedData(it.results ?: emptyList(), it.total_pages ?: 0) }
    }

    override fun reset() {
        super.reset()
        movieId = null
    }

}