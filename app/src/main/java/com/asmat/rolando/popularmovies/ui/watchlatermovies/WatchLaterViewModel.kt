package com.asmat.rolando.popularmovies.ui.watchlatermovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.asmat.rolando.popularmovies.model.PaginatedRequest
import com.asmat.rolando.popularmovies.model.mappers.MovieMapper
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.common.BaseMovieGridViewModel
import com.asmat.rolando.popularmovies.ui.common.PaginatedMovieGridViewModel

class WatchLaterViewModel(private val moviesRepository: MoviesRepository) : BaseMovieGridViewModel() {

    override val movies by lazy {
        Transformations.map(moviesRepository.getAllWatchLaterMovies()) { favoriteMovies ->
            favoriteMovies.map { favoriteMovie ->
                MovieMapper.from(favoriteMovie)
            }
        }
    }

    override val error: LiveData<Throwable> = MutableLiveData()

    override fun load() { }

}