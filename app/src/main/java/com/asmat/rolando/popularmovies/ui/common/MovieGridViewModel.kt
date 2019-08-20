package com.asmat.rolando.popularmovies.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.model.mappers.MovieMapper

/**
 * Base view model class for a grid of Movie items
 */
abstract class MovieGridViewModel : BaseMovieGridViewModel() {

    override val movies by lazy {
        Transformations.map(dataSource) { movies ->
            movies.map { MovieMapper.from(it) }
        }
    }

    override val error : LiveData<Throwable> = MutableLiveData()

    /**
     * Data source
     */
    abstract val dataSource: LiveData<List<FavoriteMovie>>

    override fun load() { }

}