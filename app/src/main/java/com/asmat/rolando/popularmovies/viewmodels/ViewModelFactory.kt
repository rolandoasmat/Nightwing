package com.asmat.rolando.popularmovies.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.asmat.rolando.popularmovies.model.MoviesRepository

class ViewModelFactory(private val moviesRepository: MoviesRepository,
                       private val movieID: Int) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(moviesRepository, movieID) as T
    }

}