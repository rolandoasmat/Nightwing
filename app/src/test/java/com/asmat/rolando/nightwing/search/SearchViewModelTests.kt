package com.asmat.rolando.nightwing.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.asmat.rolando.nightwing.model.mappers.UiModelMapper
import com.asmat.rolando.nightwing.networking.models.MoviesResponse
import com.asmat.rolando.nightwing.networking.models.PersonsResponse
import com.asmat.rolando.nightwing.networking.models.TvShowsResponse
import com.asmat.rolando.nightwing.repositories.MoviesRepository
import com.asmat.rolando.nightwing.repositories.PeopleRepository
import com.asmat.rolando.nightwing.repositories.TvShowsRepository
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SearchViewModelTests {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock lateinit var moviesRepository: MoviesRepository
    @Mock lateinit var peopleRepository: PeopleRepository
    @Mock lateinit var tvShowsRepository: TvShowsRepository
    @Mock lateinit var searchTvShowsPaginatedRequest: SearchTvShowsPaginatedRequest
    @Mock lateinit var searchTvShowsPaginatedRequestData: MutableLiveData<List<TvShowsResponse.Item>>
    @Mock lateinit var mapper: UiModelMapper

    private val movieSearchResultsData = MutableLiveData<List<MoviesResponse.Movie>>()
    private val personsSearchResultsData = MutableLiveData<List<PersonsResponse.Person>>()

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        whenever(moviesRepository.movieSearchResultsData()).thenReturn(movieSearchResultsData)
        whenever(peopleRepository.personsSearchResultsData()).thenReturn(personsSearchResultsData)
        whenever(searchTvShowsPaginatedRequest.data()).thenReturn(searchTvShowsPaginatedRequestData)
        whenever(tvShowsRepository.searchTvShowsPaginatedRequest()).thenReturn(searchTvShowsPaginatedRequest)
        viewModel = SearchViewModel(moviesRepository, peopleRepository, tvShowsRepository, mapper)
    }

    @Test
    fun `setSearchTerm then results load`() {
        // Arrange
        val searchText = "Jurassic Park"
        viewModel.results.observeForever {  }

        // Act
        viewModel.setSearchTerm(searchText)

        // Assert
        verify(moviesRepository).setMovieSearchQueryText(searchText)
        verify(moviesRepository).loadMovieSearchResults()
    }

    @Test
    fun `setSearchTerm in Persons search mode, then results load`() {
        // Arrange
        val searchText = "Andre Asmat"
        viewModel.results.observeForever {  }

        // Act
        viewModel.setSearchMode(SearchViewModel.SearchMode.PEOPLE)
        viewModel.setSearchTerm(searchText)

        // Assert
        verify(peopleRepository).setPersonsSearchQueryText(searchText)
        verify(peopleRepository).loadPersonsSearchResults()
    }

}