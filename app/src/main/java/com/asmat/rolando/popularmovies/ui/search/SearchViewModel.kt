package com.asmat.rolando.popularmovies.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.model.mappers.DataModelMapper
import com.asmat.rolando.popularmovies.model.mappers.UiModelMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository

class SearchViewModel(
        val moviesRepository: MoviesRepository,
        val peopleRepository: PeopleRepository,
        val uiModelMapper: UiModelMapper,
        val dataModelMapper: DataModelMapper): ViewModel() {

    private val _movies =  MutableLiveData<List<MovieUiModel>>()
    val movies: LiveData<List<MovieUiModel>>
        get() { return  _movies }

    private val _persons =  MutableLiveData<List<PersonUiModel>>()
    val persons: LiveData<List<PersonUiModel>>
        get() { return _persons}

    private val searchTerm = MutableLiveData<String>()

    private val searchMode = MutableLiveData<SearchMode>()

    fun search(newSearchTerm: String) {
        searchTerm.value = newSearchTerm
    }

    fun loadMore() {

    }

    fun searchMovies() {
        searchMode.value = SearchMode.MOVIES
    }

    fun searchPeople() {
        searchMode.value = SearchMode.PEOPLE
    }

    private enum class SearchMode {
        MOVIES,
        PEOPLE
    }

    private fun search() {
        moviesRepository.searchMoviesPaginatedRequest
    }

    data class MovieUiModel(val posterURL: String, val movieTitle: String)

    data class PersonUiModel(val profileURL: String, val personName: String)

}