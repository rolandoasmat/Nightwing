package com.asmat.rolando.popularmovies.ui.fragments

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.asmat.rolando.popularmovies.ui.adapters.grid.FavoriteMoviesGridAdapter
import com.asmat.rolando.popularmovies.database.DatabaseManager
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.model.mappers.MovieMapper
import com.asmat.rolando.popularmovies.ui.adapters.MovieAdapterOnClickHandler
import com.asmat.rolando.popularmovies.ui.adapters.grid.BaseMoviesGridAdapter

class FavoriteMoviesGridFragment : BaseGridFragment() {

    override val movieSource: LiveData<List<Movie>>
        get() {
            val source = DatabaseManager(context!!).getAllFavoriteMovies()
            return Transformations.map(source) { favoriteMovies ->
                favoriteMovies.map {
                    MovieMapper.from(it)
                }
            }
        }

    override fun getAdapter(handler: MovieAdapterOnClickHandler): BaseMoviesGridAdapter {
        return FavoriteMoviesGridAdapter(handler)
    }

}