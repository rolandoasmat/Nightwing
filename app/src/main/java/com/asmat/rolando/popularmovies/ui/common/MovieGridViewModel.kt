package com.asmat.rolando.popularmovies.ui.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.model.PagedData
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailsUIModel
import com.asmat.rolando.popularmovies.utilities.URLUtils

/**
 * Base view model class for a grid of Movie items
 */
abstract class MovieGridViewModel : ViewModel()  {

    val movies by lazy {
        Transformations.map(pagedData.data) { movies ->
            map(movies)
        }
    }
    val loading by lazy { pagedData.loading }
    val loadingMore by lazy { pagedData.loadingMore }
    val error by lazy { pagedData.error }
    val errorLoadingMore by lazy { pagedData.errorLoadingMore }
    val navigationEvent = MutableLiveData<NavigationEvent>()

    /**
     * Paginated data source
     */
    abstract val pagedData: PagedData<MoviesResponse.Movie>

    //region API

    /**
     * Load first page of movies
     */
    fun load() {
        pagedData.load()
    }

    /**
     * Load next page of movies
     */
    fun loadMore() {
        pagedData.loadMore()
    }

    /**
     * An movie grid item was pressed
     */
    fun itemPressed(index: Int) {
        pagedData.getItem(index)?.let { data ->
            val uiModel = map(data)
            val event = NavigationEvent.ShowMovieDetailScreen(uiModel)
            navigationEvent.value = event
        }
    }
    //endregion

    // Maps from a network to UI model
    private fun map(movies: List<MoviesResponse.Movie>): List<MovieGridItemUiModel> {
        return movies.map {
            val posterURL = it.poster_path?.let { url -> URLUtils.getImageURL342(url)}
            MovieGridItemUiModel(it.title, posterURL) }
    }

    private fun map(movie: MoviesResponse.Movie): MovieDetailsUIModel {
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