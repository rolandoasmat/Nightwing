package com.asmat.rolando.nightwing.search

import androidx.lifecycle.*
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.repositories.PeopleRepository
import com.asmat.rolando.nightwing.repositories.TvShowsRepository

class SearchViewModel(
        private val moviesRepository: MoviesRepository,
        private val peopleRepository: PeopleRepository,
        private val tvShowsRepository: TvShowsRepository,
        private val mapper: UiModelMapper): ViewModel() {

    private val _searchMode = MutableLiveData(SearchMode.MOVIES)
    val searchMode: LiveData<SearchMode>
            get() = _searchMode

    private val searchTerm = MutableLiveData<String>()

    private val _searchHint = MediatorLiveData<String>().apply {
        addSource(_searchMode) {
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

    private val _tvShows: LiveData<List<SearchResultUiModel.TvShow>> = Transformations.switchMap(tvShowsRepository.searchTvShowsPaginatedRequest.data) { response ->
        val uiModels = mapper.mapTvShows(response)
        MutableLiveData<List<SearchResultUiModel.TvShow>>(uiModels)
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
        addSource(_tvShows) {
            updateResults(it)
        }
        addSource(_searchMode) {
            searchTerm.value?.let { searchTerm ->
                searchWithTerm(searchTerm)
            }
        }
    }

    val results: LiveData<List<SearchResultUiModel>>
        get() { return _results }

    //region Public

    fun setSearchTerm(text: String) {
        searchTerm.value = text
    }

    fun loadMore() {
        _searchMode.value?.let { mode ->
            when (mode) {
                SearchMode.MOVIES -> {
                    moviesRepository.loadMoreMovieSearchResults()
                }
                SearchMode.PEOPLE -> {
                    peopleRepository.loadMorePersonsSearchResults()
                }
                SearchMode.TV_SHOWS -> {
                    tvShowsRepository.searchTvShowsPaginatedRequest.loadMore()
                }
            }
        }
    }

    fun setSearchMode(mode: SearchMode) {
        _searchMode.value = mode
    }

    //endregion

    //region Private

    private fun searchWithTerm(term: String) {
        _searchMode.value?.let { mode ->
            when (mode) {
                SearchMode.MOVIES -> {
                    moviesRepository.setMovieSearchQueryText(term)
                    moviesRepository.loadMovieSearchResults()
                }
                SearchMode.PEOPLE -> {
                    peopleRepository.setPersonsSearchQueryText(term)
                    peopleRepository.loadPersonsSearchResults()
                }
                SearchMode.TV_SHOWS -> {
                    tvShowsRepository.searchTvShowsPaginatedRequest.setSearchTerm(term)
                    tvShowsRepository.searchTvShowsPaginatedRequest.load()
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
            SearchMode.TV_SHOWS -> {
                _searchHint.value = "Search tv show"
            }
        }
    }

    private fun updateResults(results: List<SearchResultUiModel>) {
        _results.postValue(results)
    }

    //endregion

    enum class SearchMode {
        MOVIES,
        PEOPLE,
        TV_SHOWS
    }

    sealed class SearchResultUiModel(val id: Int, val imageURL: String, val title: String) {
        class Movie(id: Int, posterURL: String, movieTitle: String): SearchResultUiModel(id, posterURL, movieTitle)
        class Person(id: Int, profileURL: String, personName: String): SearchResultUiModel(id, profileURL, personName)
        class TvShow(id: Int, posterURL: String, name: String): SearchResultUiModel(id, posterURL, name)
    }
}