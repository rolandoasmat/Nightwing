package com.asmat.rolando.popularmovies.model

import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import io.reactivex.Single
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

data class NowPlayingPaginatedRequest(private val tmdbClient: TheMovieDBClient,
                                      private val computationScheduler: Scheduler) : PaginatedRequest<MoviesResponse.Movie>(AndroidSchedulers.mainThread()) {

    override fun fetchData(pageToLoad: Int): Single<PagedData<MoviesResponse.Movie>> {
        return tmdbClient
                .getNowPlayingMovies(pageToLoad)
                .subscribeOn(computationScheduler)
                .map { PagedData(it.results, it.total_pages) }
    }

}