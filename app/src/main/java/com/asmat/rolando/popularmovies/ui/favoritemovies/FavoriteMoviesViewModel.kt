package com.asmat.rolando.popularmovies.ui.favoritemovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.model.mappers.MovieMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.ui.common.BaseMovieGridViewModel
import com.asmat.rolando.popularmovies.ui.common.MovieGridItemUiModel
import com.asmat.rolando.popularmovies.ui.common.MovieGridViewModel

class FavoriteMoviesViewModel(private val moviesRepository: MoviesRepository) : MovieGridViewModel() {

    override val dataSource: LiveData<List<FavoriteMovie>>
        get()  {
            return moviesRepository.getAllFavoriteMovies()
        }


}