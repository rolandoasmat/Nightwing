package com.asmat.rolando.popularmovies.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.repositories.MoviesRepository

class ViewModelFactory(private val moviesRepository: MoviesRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(moviesRepository) as T
    }

}