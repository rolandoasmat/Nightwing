package com.asmat.rolando.popularmovies.search

import com.asmat.rolando.popularmovies.networking.models.MoviesResponse
import com.asmat.rolando.popularmovies.networking.models.PersonsResponse
import com.asmat.rolando.popularmovies.utilities.URLUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchDataModelsMapper @Inject constructor() {

    fun mapMovies(response: List<MoviesResponse.Movie>): List<SearchViewModel.SearchResultUiModel.Movie> {
        val uiModels = mutableListOf<SearchViewModel.SearchResultUiModel.Movie>()
        response.forEach{ data ->
            val id = data.id
            val url = data.poster_path?.let { URLUtils.getImageURL342(it) } ?: ""
            val title = data.original_title
            if (id != null && url.isNotEmpty() && title != null) {
                uiModels.add(SearchViewModel.SearchResultUiModel.Movie(id, url, title))
            }
        }
        return uiModels
    }

    fun mapPersons(response: List<PersonsResponse.Person>): List<SearchViewModel.SearchResultUiModel.Person> {
        val uiModels = mutableListOf<SearchViewModel.SearchResultUiModel.Person>()
        response.forEach{ data ->
            val id = data.id
            val url = data.profile_path?.let { URLUtils.getImageURL342(it) } ?: ""
            val name = data.name
            if (id != null && url.isNotEmpty() && name.isNotEmpty()) {
                uiModels.add(SearchViewModel.SearchResultUiModel.Person(id, url, name))
            }
        }
        return uiModels
    }

}