package com.asmat.rolando.popularmovies.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.ui.cast_detail.CastDetailViewModel

class ViewModelFactory(private val moviesRepository: MoviesRepository,
                       private val peopleRepository: PeopleRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MovieDetailsViewModel::class.java) -> MovieDetailsViewModel(moviesRepository) as T
            modelClass.isAssignableFrom(CastDetailViewModel::class.java) -> CastDetailViewModel(peopleRepository) as T
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

}