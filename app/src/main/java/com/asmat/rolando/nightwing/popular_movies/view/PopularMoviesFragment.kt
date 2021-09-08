package com.asmat.rolando.nightwing.popular_movies.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.asmat.rolando.nightwing.NightwingApplication
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.popular_movies.viewmodel.PopularMoviesViewModel
import com.asmat.rolando.nightwing.movies.MovieGridItemComparator
import com.asmat.rolando.nightwing.movies.MoviesPagingDataAdapter
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_popular_movies.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularMoviesFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: PopularMoviesViewModel by viewModels { viewModelFactory }

    val pagingAdapter = MoviesPagingDataAdapter(null, MovieGridItemComparator)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as NightwingApplication).component().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_popular_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popularMoviesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        popularMoviesRecyclerView.adapter = pagingAdapter
        viewModel.popularMovies.observe(viewLifecycleOwner) { pagingData ->
            Log.v("RAA", "Got new paging data: $pagingData")
            pagingAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }
    }

    fun goToMovieDetailsScreen(movieID: Int) {
        val action = PopularMoviesFragmentDirections.actionPopularMoviesGridToMovieDetailsScreen(movieID)
        findNavController().navigate(action)
    }
}