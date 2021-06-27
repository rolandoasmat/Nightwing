package com.asmat.rolando.nightwing.ui.popularmovies

import android.os.Bundle
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
import com.asmat.rolando.nightwing.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_popular_movies.*
import kotlinx.coroutines.flow.collectLatest
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }

    fun goToMovieDetailsScreen(movieID: Int) {
        val action = PopularMoviesFragmentDirections.actionPopularMoviesGridToMovieDetailsScreen(movieID)
        findNavController().navigate(action)
    }
}