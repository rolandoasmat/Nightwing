package com.asmat.rolando.popularmovies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.ui.castdetails.CastDetailsViewModel
import com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits.PersonMovieCreditsViewModel
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailsViewModel
import com.asmat.rolando.popularmovies.ui.popularmovies.PopularMoviesViewModel

class ViewModelFactory(private val moviesRepository: MoviesRepository,
                       private val peopleRepository: PeopleRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PopularMoviesViewModel::class.java) -> PopularMoviesViewModel(moviesRepository) as T
            modelClass.isAssignableFrom(MovieDetailsViewModel::class.java) -> MovieDetailsViewModel(moviesRepository) as T
            modelClass.isAssignableFrom(CastDetailsViewModel::class.java) -> CastDetailsViewModel(peopleRepository) as T
            modelClass.isAssignableFrom(PersonMovieCreditsViewModel::class.java) -> PersonMovieCreditsViewModel(peopleRepository) as T
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

}