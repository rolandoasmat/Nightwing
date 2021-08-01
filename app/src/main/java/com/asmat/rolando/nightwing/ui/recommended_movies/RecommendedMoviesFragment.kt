package com.asmat.rolando.nightwing.ui.recommended_movies

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.asmat.rolando.nightwing.ui.moviegrid.paginated.PaginatedMovieGridFragment
//import com.asmat.rolando.nightwing.ui.popularmovies.PopularMoviesFragmentDirections

class RecommendedMoviesFragment: PaginatedMovieGridFragment() {

    override val viewModel: RecommendedMoviesViewModel by viewModels { viewModelFactory }

    val args: RecommendedMoviesFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.setMovieId(args.movieIdArg)
    }

    override fun goToMovieDetailsScreen(movieID: Int) {
        val action = RecommendedMoviesFragmentDirections.actionRecommendedMoviesGridToMovieDetailsScreen(movieID)
        findNavController().navigate(action)
    }

}