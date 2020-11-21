package com.asmat.rolando.nightwing.ui.watchlatermovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.nightwing.ui.moviegrid.BaseMovieGridFragment
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory

class WatchLaterMoviesGridFragment: BaseMovieGridFragment() {

    override val viewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository, dataModelMapper, uiModelMapper, deepLinksUtils)).get(WatchLaterViewModel::class.java) }

    override fun goToMovieDetailsScreen(movieID: Int) {
        TODO("Not yet implemented")
    }

}