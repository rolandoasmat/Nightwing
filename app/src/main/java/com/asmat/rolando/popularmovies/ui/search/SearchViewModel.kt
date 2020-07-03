package com.asmat.rolando.popularmovies.ui.search

import androidx.lifecycle.*
import com.asmat.rolando.popularmovies.model.mappers.DataModelMapper
import com.asmat.rolando.popularmovies.model.mappers.UiModelMapper
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.utilities.URLUtils

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

    private val _movies: LiveData<List<SearchResultUiModel.Movie>> = Transformations.switchMap(moviesRepository.searchMoviesPaginatedRequest.data) { response ->
        val uiModels = mutableListOf<SearchResultUiModel.Movie>()
        response.forEach{ data ->
            val id = data.id
            val url = data.poster_path?.let { URLUtils.getImageURL342(it) } ?: ""
            val title = data.original_title
            if (id != null && url.isNotEmpty() && title != null) {
                uiModels.add(SearchResultUiModel.Movie(id, url, title))
            }
        }
        MutableLiveData<List<SearchResultUiModel.Movie>>(uiModels)
    }

    private val _persons: LiveData<List<SearchResultUiModel.Person>> = Transformations.switchMap(peopleRepository.searchPersonsPaginatedRequest.data) { response ->
        val uiModels = mutableListOf<SearchResultUiModel.Person>()
        response.forEach{ data ->
            val id = data.id
            val url = data.profile_path?.let { URLUtils.getImageURL342(it) } ?: ""
            val name = data.name
            if (id != null && url.isNotEmpty() && name.isNotEmpty()) {
                uiModels.add(SearchResultUiModel.Person(id, url, name))
            }
        }
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