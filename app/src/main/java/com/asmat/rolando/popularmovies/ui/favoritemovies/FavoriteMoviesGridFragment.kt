package com.asmat.rolando.popularmovies.ui.favoritemovies

import androidx.lifecycle.ViewModelProviders
import com.asmat.rolando.popularmovies.ui.common.*
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory

class FavoriteMoviesGridFragment : BaseMovieGridFragment() {

//    @Inject
//    lateinit var moviesRepository: MoviesRepository
//
//    @Inject
//    lateinit var peopleRepository: PeopleRepository

    override val viewModel: MovieGridViewModel
        get() = ViewModelProviders.of(this, ViewModelFactory(moviesRepository, peopleRepository)).get(FavoriteMoviesViewModel::class.java)

}