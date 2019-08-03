package com.asmat.rolando.popularmovies.ui.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.model.Movie
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MoviesResponse
import com.asmat.rolando.popularmovies.utilities.URLUtils
import io.reactivex.Single

abstract class MovieGridViewModel : ViewModel()  {

    val movies = MutableLiveData<List<MovieGridItemUiModel>>()
    val loading = MutableLiveData<Boolean>()
    val loadingMore = MutableLiveData<Boolean>()
    val error = MutableLiveData<Throwable>()
    val errorLoadingMore = MutableLiveData<Throwable>()
    val navigationEvent = MutableLiveData<NavigationEvent>()

    private var pageToFetch = 1
    private var totalNumOfPages: Int? = null

    abstract fun fetchMovies(page: Int): Single<MoviesResponse>
    abstract fun getMovieData(index: Int): Movie

    //region
    fun load() {
        pageToFetch = 1
        loading.value = true
        fetchMovies(pageToFetch)
                .subscribe({ result ->
                    handleMoviesResponse(result)
                    loading.value = false
                }, { error ->
                    this.error.value = error
                    loading.value = false
                })
    }

    fun loadMore() {
        loadingMore.value = true
        fetchMovies(pageToFetch)
                .subscribe({ result ->
                    handleMoviesResponse(result)
                    loadingMore.value = false
                }, { error ->
                    this.errorLoadingMore.value = error
                    loadingMore.value = false
                })
    }

    fun itemPressed(index: Int) {
        val data = getMovieData(index)
        val event = NavigationEvent.ShowMovieDetailScreen(data)
        navigationEvent.value = event
    }
    //endregion

    private fun handleMoviesResponse(result: MoviesResponse) {
        this.totalNumOfPages = result.total_pages
        val currentItems:  List<MovieGridItemUiModel> = this.movies.value ?: emptyList()
        val newItems: List<MovieGridItemUiModel> = map(result.results)
        movies.value = currentItems + newItems
        pageToFetch++
    }

    private fun map(movies: List<MoviesResponse.Movie>): List<MovieGridItemUiModel> {
        return movies.map {
            val posterURL = it.poster_path?.let { url -> URLUtils.getImageURL342(url)}
            MovieGridItemUiModel(it.title, posterURL) }
    }

    sealed class NavigationEvent {
        class ShowMovieDetailScreen(val data: Movie): NavigationEvent()
    }

}