package com.asmat.rolando.popularmovies.ui.favoritemovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.model.mappers.DataModelMapper
import com.asmat.rolando.popularmovies.model.mappers.UiModelMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.moviegrid.BaseMovieGridViewModel

class FavoriteMoviesViewModel(moviesRepository: MoviesRepository,
                              uiModelMapper: UiModelMapper,
                              private val dataModelMapper: DataModelMapper) : BaseMovieGridViewModel(moviesRepository, uiModelMapper) {

    override val movies: LiveData<List<Movie>> by lazy {
        Transformations.map(moviesRepository.getAllFavoriteMovies()) {
            dataModelMapper.mapFavoriteMovies(it)
        }
    }

    override val loading = MutableLiveData<Boolean>()

    override val error = MutableLiveData<Throwable>()

}