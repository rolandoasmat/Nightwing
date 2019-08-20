package com.asmat.rolando.popularmovies.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailsUIModel
import com.asmat.rolando.popularmovies.utilities.URLUtils

abstract class BaseMovieGridViewModel : ViewModel()  {

    val navigationEvent = MutableLiveData<NavigationEvent>()

    /**
     * Movies data source
     */
    abstract val movies: LiveData<List<MovieGridItemUiModel>>

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
     * Get the data of movie at a specific index
     */
    abstract fun getMovieAt(index: Int): MoviesResponse.Movie?

    /**
     * An movie grid item was pressed
     */
    fun itemPressed(index: Int) {
        getMovieAt(index)?.let { data ->
            val uiModel = map(data)
            val event = NavigationEvent.ShowMovieDetailScreen(uiModel)
            navigationEvent.value = event
        }
    }
    //endregion

    // Maps from a network to UI model
    protected fun map(movies: List<MoviesResponse.Movie>): List<MovieGridItemUiModel> {
        return movies.map {
            val posterURL = it.poster_path?.let { url -> URLUtils.getImageURL342(url)}
            MovieGridItemUiModel(it.title, posterURL) }
    }

    protected fun map(movie: MoviesResponse.Movie): MovieDetailsUIModel {
        val posterURL = movie.poster_path?.let { url -> URLUtils.getImageURL342(url)}
        val backdropURL = movie.backdrop_path?.let { url -> URLUtils.getImageURL780(url)}
        return MovieDetailsUIModel(posterURL, movie.overview, movie.release_date, movie.id, movie.title, backdropURL, movie.vote_average)
    }

    /**
     * Navigation events
     */
    sealed class NavigationEvent {
        // Navigate to the Movie details screen
        class ShowMovieDetailScreen(val uiModel: MovieDetailsUIModel): NavigationEvent()
    }
}