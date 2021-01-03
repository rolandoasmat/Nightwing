package com.asmat.rolando.nightwing.ui.watchlatermovies

import androidx.fragment.app.viewModels
import com.asmat.rolando.nightwing.ui.moviegrid.BaseMovieGridFragment

class WatchLaterMoviesGridFragment: BaseMovieGridFragment() {

    override val viewModel: WatchLaterViewModel by viewModels { viewModelFactory }

    override fun goToMovieDetailsScreen(movieID: Int) {
        TODO("Not yet implemented")
    }
}