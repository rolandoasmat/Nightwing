package com.asmat.rolando.nightwing.ui.upcomingmovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.nightwing.ui.moviegrid.paginated.PaginatedMovieGridFragment
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory

class UpcomingMoviesFragment: PaginatedMovieGridFragment() {

    override val viewModel: UpcomingMoviesViewModel
        get() = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository, dataModelMapper, uiModelMapper, deepLinksUtils)).get(UpcomingMoviesViewModel::class.java)

    override fun goToMovieDetailsScreen(movieID: Int) {
        TODO("Not yet implemented")
    }

}