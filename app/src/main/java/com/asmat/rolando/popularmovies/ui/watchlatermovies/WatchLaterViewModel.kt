package com.asmat.rolando.popularmovies.ui.watchlatermovies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.asmat.rolando.popularmovies.model.mappers.MovieMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.common.BaseMovieGridViewModel

class WatchLaterViewModel(private val moviesRepository: MoviesRepository) : BaseMovieGridViewModel() {

    override val movies by lazy { Transformations.map(moviesRepository.getAllWatchLaterMovies()) {
        it.map { MovieMapper.from(it) }
    } }

    override val error = MutableLiveData<Throwable>()

    override fun load() { }

}