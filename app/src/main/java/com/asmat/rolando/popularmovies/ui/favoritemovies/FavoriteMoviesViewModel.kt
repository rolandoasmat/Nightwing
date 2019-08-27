package com.asmat.rolando.popularmovies.ui.favoritemovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.model.mappers.MovieMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.moviegrid.BaseMovieGridViewModel
import javax.inject.Inject

class FavoriteMoviesViewModel @Inject constructor(
        moviesRepository: MoviesRepository
) : BaseMovieGridViewModel(moviesRepository) {

    override val movies: LiveData<List<Movie>> by lazy {
        Transformations.map(moviesRepository.getAllFavoriteMovies()) { it ->
            it.map { MovieMapper.from(it) }
        }
    }

    override val loading = MutableLiveData<Boolean>()

    override val error = MutableLiveData<Throwable>()

    override fun load() {}

}