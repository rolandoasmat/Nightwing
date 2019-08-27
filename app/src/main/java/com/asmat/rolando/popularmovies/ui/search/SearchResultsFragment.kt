package com.asmat.rolando.popularmovies.ui.search

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridFragment

class SearchResultsFragment : PaginatedMovieGridFragment() {

    override val viewModel: SearchMoviesViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(SearchMoviesViewModel::class.java)

    fun setSearchQuery(searchQuery: String) {
        viewModel.searchTermChanged(searchQuery)
        viewModel.load()
    }
}