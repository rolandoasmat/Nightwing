package com.asmat.rolando.popularmovies.ui.nowplayingmovies

import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.common.MovieGridViewModel
import io.reactivex.Single

class NowPlayingMoviesViewModel(private val moviesRepository: MoviesRepository) : MovieGridViewModel() {

    override fun getMovieData(index: Int): Movie {
        return moviesRepository.getNowPlayingMovieAt(index)
    }

    override fun fetchMovies(page: Int): Single<MoviesResponse> {
        return moviesRepository.getNowPlayingMovies(page)
    }

}