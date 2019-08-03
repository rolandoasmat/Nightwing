package com.asmat.rolando.popularmovies.ui.watchlatermovies

import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.common.MovieGridViewModel
import io.reactivex.Single

class WatchLaterViewModel(private val moviesRepository: MoviesRepository) : MovieGridViewModel() {

    override fun getMovieData(index: Int): Movie {
        return moviesRepository.getPopularMovieAt(index)
    }

    override fun fetchMovies(page: Int): Single<MoviesResponse> {
        return moviesRepository.getPopularMovies(page)
    }

}