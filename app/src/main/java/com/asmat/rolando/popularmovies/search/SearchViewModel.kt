package com.asmat.rolando.popularmovies.search

import androidx.lifecycle.*
import com.asmat.rolando.popularmovies.model.mappers.UiModelMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository

class SearchViewModel(
        private val moviesRepository: MoviesRepository,
        private val peopleRepository: PeopleRepository,
        private val mapper: UiModelMapper): ViewModel() {

    private val searchMode = MutableLiveData(SearchMode.MOVIES)
    private val searchTerm = MutableLiveData<String>()

    private val _searchHint = MediatorLiveData<String>().apply {
        addSource(searchMode) {
            searchModeChanged(it)
        }
    }
    val searchHint: LiveData<String>
        get() { return _searchHint }

    private val _movies: LiveData<List<SearchResultUiModel.Movie>> = Transformations.switchMap(moviesRepository.movieSearchResultsData()) { response ->
        val uiModels = mapper.mapMovies(response)
        MutableLiveData<List<SearchResultUiModel.Movie>>(uiModels)
    }

    private val _persons: LiveData<List<SearchResultUiModel.Person>> = Transformations.switchMap(peopleRepository.personsSearchResultsData()) { response ->
        val uiModels = mapper.mapPersons(response)
        MutableLiveData<List<SearchResultUiModel.Person>>(uiModels)
    }

    private val _results = MediatorLiveData<List<SearchResultUiModel>>().apply {
        addSource(searchTerm) {
            searchWithTerm(it)
        }
        addSource(_movies) {
            updateResults(it)
        }
        addSource(_persons) {
            updateResults(it)
        }
        addSource(searchMode) {
            updateResults(emptyList())
        }
    }

    val results: LiveData<List<SearchResultUiModel>>
        get() { return _results }

    //region Public

    fun setSearchTerm(text: String) {
        searchTerm.value = text
    }

    fun loadMore() {
        searchMode.value?.let { mode ->
            when (mode) {
                SearchMode.MOVIES -> {
                    moviesRepository.loadMoreMovieSearchResults()
                }
                SearchMode.PEOPLE -> {
                    peopleRepository.loadMorePersonsSearchResults()
                }
            }
        }
    }

    fun setSearchMode(mode: SearchMode) {
        searchMode.value = mode
    }

    //endregion

    //region Private

    private fun searchWithTerm(term: String) {
        searchMode.value?.let { mode ->
            when (mode) {
                SearchMode.MOVIES -> {
                    moviesRepository.setMovieSearchQueryText(term)
                    moviesRepository.loadMovieSearchResults()
                }
                SearchMode.PEOPLE -> {
                    peopleRepository.setPersonsSearchQueryText(term)
                    peopleRepository.loadPersonsSearchResults()
                }
            }
        }
    }

    private fun searchModeChanged(mode: SearchMode) {
        when (mode) {
            SearchMode.MOVIES -> {
                _searchHint.value = "Search movie title"
            }
            SearchMode.PEOPLE -> {
                _searchHint.value = "Search actor name"
            }
        }
    }

    private fun updateResults(results: List<SearchResultUiModel>) {
        _results.postValue(results)
    }

    //endregion

    enum class SearchMode {
        MOVIES,
        PEOPLE
    }
    sealed class SearchResultUiModel(val id: Int, val imageURL: String, val title: String) {
        class Movie(id: Int, posterURL: String, movieTitle: String): SearchResultUiModel(id, posterURL, movieTitle)
        class Person(id: Int, profileURL: String, personName: String): SearchResultUiModel(id, profileURL, personName)
    }
}