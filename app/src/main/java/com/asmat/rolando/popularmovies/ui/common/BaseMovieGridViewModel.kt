package com.asmat.rolando.popularmovies.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailsUIModel
import com.asmat.rolando.popularmovies.utilities.URLUtils

abstract class BaseMovieGridViewModel : ViewModel()  {

    val navigationEvent = MutableLiveData<NavigationEvent>()

    protected var moviesData: List<Movie>? = null

    /**
     * Movies data source
     */
    abstract val moviesUIModels: LiveData<List<MovieGridItemUiModel>>

    /**
     * Any error related to fetching movies
     */
    abstract val error: LiveData<Throwable>

    //region API

    /**
     * Load movies
     */
    abstract fun load()

    /**
     * An movie grid item was pressed
     */
    fun itemPressed(index: Int) {
        moviesData?.get(index)?.let { data ->
            val uiModel = map(data)
            val event = NavigationEvent.ShowMovieDetailScreen(uiModel)
            navigationEvent.value = event
        }
    }
    //endregion

    protected fun map(movies: List<Movie>): List<MovieGridItemUiModel> {
        return movies.map {
            val posterURL = it.posterPath?.let { url -> URLUtils.getImageURL342(url)}
            MovieGridItemUiModel(it.title, posterURL) }
    }

    protected fun map(movie: Movie): MovieDetailsUIModel {
        val posterURL = movie.posterPath?.let { url -> URLUtils.getImageURL342(url)}
        val backdropURL = movie.backdropPath?.let { url -> URLUtils.getImageURL780(url)}
        return MovieDetailsUIModel(posterURL, movie.overview, movie.releaseDate, movie.id, movie.title, backdropURL, movie.voteAverage)
    }

    /**
     * Navigation events
     */
    sealed class NavigationEvent {
        // Navigate to the Movie details screen
        class ShowMovieDetailScreen(val uiModel: MovieDetailsUIModel): NavigationEvent()
    }
}