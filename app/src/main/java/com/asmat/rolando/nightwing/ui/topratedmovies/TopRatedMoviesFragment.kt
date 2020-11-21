package com.asmat.rolando.nightwing.ui.topratedmovies

import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.asmat.rolando.nightwing.ui.moviegrid.paginated.PaginatedMovieGridFragment
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory

class TopRatedMoviesFragment: PaginatedMovieGridFragment() {

    override val viewModel: TopRatedMoviesViewModel
        get() = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository, dataModelMapper, uiModelMapper, deepLinksUtils)).get(TopRatedMoviesViewModel::class.java)

    override fun goToMovieDetailsScreen(movieID: Int) {
        val action = TopRatedMoviesFragmentDirections.actionTopRatedMoviesGridToMovieDetailsScreen(movieID)
        findNavController().navigate(action)
    }

}