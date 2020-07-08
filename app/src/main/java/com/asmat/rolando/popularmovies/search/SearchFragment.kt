package com.asmat.rolando.popularmovies.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.asmat.rolando.popularmovies.MovieNightApplication
import com.asmat.rolando.popularmovies.R
import com.asmat.rolando.popularmovies.extensions.setNearBottomScrollListener
import com.asmat.rolando.popularmovies.ui.castdetails.CastDetailsActivity
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailsActivity
import com.asmat.rolando.popularmovies.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

// TODO update search to look like instagram
class SearchFragment: Fragment(), SearchAdapter.Callbacks {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    val viewModel: SearchViewModel by viewModels{ viewModelFactory }

    private var adapter: SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as? MovieNightApplication)?.component()?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        searchView?.queryHint = "Search for a movie, actor, tv show, etc."
        searchView?.setIconifiedByDefault(false)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearchTerm(query ?: "")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchTerm(newText ?: "")
                return false
            }
        })
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter(this)
        searchResultsRecyclerView?.adapter = adapter
        searchResultsRecyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)
        searchResultsRecyclerView?.setNearBottomScrollListener {
            viewModel.loadMore()
        }
    }

    private fun observeViewModel() {
        viewModel.results.observe(viewLifecycleOwner, Observer {
            updateResults(it)
        })
    }

    private fun updateResults(items: List<SearchViewModel.SearchResultUiModel>) {
        adapter?.setData(items)
    }

    override fun openMovieDetails(id: Int) {
        val intent = MovieDetailsActivity.createIntent(requireContext(), id)
        startActivity(intent)
    }

    override fun openActorDetails(id: Int) {
        val intent = CastDetailsActivity.createIntent(requireContext(), id)
        startActivity(intent)
    }
}