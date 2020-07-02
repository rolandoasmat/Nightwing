package com.asmat.rolando.popularmovies.ui.search

import androidx.lifecycle.*
import com.asmat.rolando.popularmovies.model.mappers.DataModelMapper
import com.asmat.rolando.popularmovies.model.mappers.UiModelMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository

class SearchViewModel(
        private val moviesRepository: MoviesRepository,
        private val peopleRepository: PeopleRepository,
        private val uiModelMapper: UiModelMapper,
        private val dataModelMapper: DataModelMapper): ViewModel() {

    private val searchMode = MutableLiveData(SearchMode.MOVIES)
    private val searchTerm = MutableLiveData("")

    private val _searchHint = MediatorLiveData<String>().apply {
        addSource(searchMode) {
            searchModeChanged(it)
        }
    }
    val searchHint: LiveData<String>
        get() { return _searchHint }

    private val _movies: LiveData<List<SearchResultUiModel.Movie>> =  Transformations.switchMap(moviesRepository.searchMoviesPaginatedRequest.data) { response ->
        val uiModels = response.map { SearchResultUiModel.Movie(it.poster_path ?: "", it.original_title ?: "") }
        val liveData = MutableLiveData<List<SearchResultUiModel.Movie>>()
        liveData.value = uiModels
        liveData
    }

    private val _persons: LiveData<List<SearchResultUiModel.Person>> =  Transformations.switchMap(peopleRepository.searchPersonsPaginatedRequest.data) { response ->
        val uiModels = response.map { SearchResultUiModel.Person(it.profile_path ?: "", it.name ?: "") }
        val liveData = MutableLiveData<List<SearchResultUiModel.Person>>()
        liveData.value = uiModels
        liveData
    }

    private val _results = MediatorLiveData<List<SearchResultUiModel>>().apply {
        addSource(searchTerm) {
            searchWithTerm(it)
        }
        addSource(_movies) {
            handleResults(it)
        }
        addSource(_persons) {
            handleResults(it)
        }
    }

    private fun handleResults(results: List<SearchResultUiModel>) {
        _results.postValue(results)
    }

    val results: LiveData<List<SearchResultUiModel>>
        get() { return _results }

    //region Public

    fun setSearchTerm(text: String) {
        searchTerm.value = text
    }

    fun loadMore() {

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
                    moviesRepository.searchMoviesPaginatedRequest.setSearchTerm(term)
                    moviesRepository.searchMoviesPaginatedRequest.load()
                }
                SearchMode.PEOPLE -> {
                    peopleRepository.searchPersonsPaginatedRequest.setSearchTerm(term)
                    peopleRepository.searchPersonsPaginatedRequest.load()
                }
            }
        }
    }

    private fun searchModeChanged(mode: SearchMode) {
        when (mode) {
            SearchMode.MOVIES -> {
                _searchHint.value = "Search movies"
            }
            SearchMode.PEOPLE -> {
                _searchHint.value = "Search actors"
            }
        }
    }

    //endregion

    enum class SearchMode {
        MOVIES,
        PEOPLE
    }
    sealed class SearchResultUiModel(val imageURL: String, val title: String) {
        class Movie(posterURL: String, movieTitle: String): SearchResultUiModel(posterURL, movieTitle)
        class Person(profileURL: String, personName: String): SearchResultUiModel(profileURL, personName)
    }
}