package com.asmat.rolando.nightwing.ui.favoritemovies

import androidx.fragment.app.viewModels
import com.asmat.rolando.nightwing.ui.moviegrid.BaseMovieGridFragment

class FavoriteMoviesGridFragment: BaseMovieGridFragment() {

    override val viewModel: FavoriteMoviesViewModel by viewModels { viewModelFactory }

    override fun goToMovieDetailsScreen(movieID: Int) {
        TODO("Not yet implemented")
    }

}