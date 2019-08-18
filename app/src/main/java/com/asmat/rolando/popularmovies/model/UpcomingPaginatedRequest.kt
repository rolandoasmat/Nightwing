package com.asmat.rolando.popularmovies.model

import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

data class UpcomingPaginatedRequest(private val tmdbClient: TheMovieDBClient) : PaginatedRequest<MoviesResponse.Movie>() {

    override fun fetchData(pageToLoad: Int): Single<PagedData<MoviesResponse.Movie>> {
        return tmdbClient
                .getUpcomingMovies(pageToLoad)
                .subscribeOn(Schedulers.computation())
                .map { PagedData(it.results, it.total_pages) }
    }

}