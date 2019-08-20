package com.asmat.rolando.popularmovies.ui.search

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridFragment
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory

class SearchResultsFragment : PaginatedMovieGridFragment() {

//    @Inject
//    lateinit var moviesRepository: MoviesRepository
//
//    @Inject
//    lateinit var peopleRepository: PeopleRepository

    override val viewModel: SearchMoviesViewModel
        get() = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository)).get(SearchMoviesViewModel::class.java)

    fun setSearchQuery(searchQuery: String) {
        viewModel.searchTermChanged(searchQuery)
        viewModel.load()
    }
}