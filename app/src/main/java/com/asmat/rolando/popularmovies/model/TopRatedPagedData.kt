package com.asmat.rolando.popularmovies.model

import com.asmat.rolando.popularmovies.networking.the.movie.db.TheMovieDBClient
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

data class TopRatedPagedData(private val tmdbClient: TheMovieDBClient): PagedData<MoviesResponse.Movie>() {

    override fun fetchData(pageToLoad: Int): Single<List<MoviesResponse.Movie>> {
        return tmdbClient
                .getPopularMovies(pageToLoad)
                .subscribeOn(Schedulers.computation())
                .map { it.results }
    }

}