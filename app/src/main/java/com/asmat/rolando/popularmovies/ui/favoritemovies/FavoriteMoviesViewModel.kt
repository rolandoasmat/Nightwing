package com.asmat.rolando.popularmovies.ui.favoritemovies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.asmat.rolando.popularmovies.model.mappers.MovieMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.common.BaseMovieGridViewModel

class FavoriteMoviesViewModel(moviesRepository: MoviesRepository) : BaseMovieGridViewModel() {

    override val movies by lazy { Transformations.map(moviesRepository.getAllFavoriteMovies()) {
        it.map { MovieMapper.from(it) }
    } }

    override val error = MutableLiveData<Throwable>()

    override fun load() { }

}