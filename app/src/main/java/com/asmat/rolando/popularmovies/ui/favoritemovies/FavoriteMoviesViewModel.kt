package com.asmat.rolando.popularmovies.ui.favoritemovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.asmat.rolando.popularmovies.model.mappers.MovieMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.common.BaseMovieGridViewModel
import com.asmat.rolando.popularmovies.ui.common.MovieGridItemUiModel

class FavoriteMoviesViewModel(private val moviesRepository: MoviesRepository) : BaseMovieGridViewModel() {

    override val moviesUIModels: LiveData<List<MovieGridItemUiModel>> = Transformations.map(moviesRepository.getAllFavoriteMovies()) { favoriteMovies ->
        val movies = favoriteMovies.map { favoriteMovie ->
            MovieMapper.from(favoriteMovie)
        }
        moviesData = movies
        map(movies)
    }

    override val error: LiveData<Throwable> = MutableLiveData()

    override fun load() { }

}