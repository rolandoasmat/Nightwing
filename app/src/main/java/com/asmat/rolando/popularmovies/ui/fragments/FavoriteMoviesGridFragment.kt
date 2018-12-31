package com.asmat.rolando.popularmovies.ui.fragments

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.asmat.rolando.popularmovies.ui.adapters.grid.FavoriteMoviesGridAdapter
import com.asmat.rolando.popularmovies.database.DatabaseManager
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.ui.adapters.MovieAdapterOnClickHandler
import com.asmat.rolando.popularmovies.ui.adapters.grid.BaseMoviesGridAdapter

class FavoriteMoviesGridFragment : BaseGridFragment() {

    override val movieSource: LiveData<List<MoviesResponse.Movie>>
        get() {
            val source = DatabaseManager(context!!).getAllFavoriteMovies()
            return Transformations.map(source) { favoriteMovies ->
                favoriteMovies.map {
                    MoviesResponse.Movie(it.poster_path, it.adult, it.overview, it.release_date,
                            emptyList(), it.id, it.original_title, it.original_language, it.title,
                            it.backdrop_path, it.popularity, it.vote_count, it.video, it.vote_average)
                }
            }
        }

    override fun getAdapter(handler: MovieAdapterOnClickHandler): BaseMoviesGridAdapter {
        return FavoriteMoviesGridAdapter(handler)
    }

}