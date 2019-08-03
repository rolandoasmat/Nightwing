package com.asmat.rolando.popularmovies.ui.search

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.ui.common.MovieGridFragment
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory
import javax.inject.Inject

class SearchResultsFragment : MovieGridFragment() {

    @Inject
    lateinit var moviesRepository: MoviesRepository

    @Inject
    lateinit var peopleRepository: PeopleRepository

    override val viewModel: SearchMoviesViewModel
        get() = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository)).get(SearchMoviesViewModel::class.java)

    fun setSearchQuery(searchQuery: String) {
        viewModel.searchTermChanged(searchQuery)
        viewModel.load()
    }
}