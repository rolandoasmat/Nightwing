package com.asmat.rolando.popularmovies.ui.favoritemovies

import com.asmat.rolando.popularmovies.ui.common.*

class FavoriteMoviesGridFragment : BaseMovieGridFragment() {

    override val viewModel: BaseMovieGridViewModel
        get() = FavoriteMoviesViewModel(moviesRepository)


}